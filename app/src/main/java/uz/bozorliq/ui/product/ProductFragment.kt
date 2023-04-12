package uz.bozorliq.ui.product

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.adapters.ProductsAdapter
import uz.bozorliq.base.AddCategoryDialog
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.base.DialogDeleteCategory
import uz.bozorliq.databinding.FragmentProductBinding
import uz.bozorliq.databinding.ItemProductBinding
import uz.bozorliq.model.product.ProductData
import uz.bozorliq.utils.*
import uz.bozorliq.utils.theme.Theme
import uz.smartavtodrom.utils.collectLA
import java.util.*


@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate), OnStartDragListener, OnCustomerListChangedListener, ProductsAdapter.OnItemClickListener, OnClickListener {

    private val vm: ProductViewModel by viewModels()
    private var productData: ArrayList<ProductData>? = null

    var categoryId = -1

    private var mAdapter: ProductsAdapter? = null
    private var mItemTouchHelper: ItemTouchHelper? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener(this)
        arguments?.getString("category_name")?.let {
            binding.title.text = it[0].uppercase() + it.substring(1).lowercase()
        }
        setupRecyclerView()
        arguments?.getInt("category_id")?.let {
            categoryId = it
            getProducts(it)
        }
        binding.addProduct.setOnClickListener(this)
        if (productData == null) {
            productData = arrayListOf()
        }
    }

    var start = false

    private fun setupRecyclerView() {

        binding.listProducts.setHasFixedSize(true)
        binding.listProducts.layoutManager = LinearLayoutManager(binding.root.context)

        mAdapter = ProductsAdapter(this, this, this)
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(mAdapter!!)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(binding.listProducts)
        binding.listProducts.adapter = mAdapter
    }

    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)

        binding.actionBar.setBackgroundColor(theme.colorPrimary)
        binding.backBtn.setColorFilter(theme.textColorPrimary, PorterDuff.Mode.MULTIPLY)
        binding.title.setTextColor(theme.textColorPrimary)
//        binding.dayModeTxt.setTextColor(theme.textColor)
//        binding.nightModeTxt.setTextColor(theme.textColor)
        binding.itemBack.setBackgroundColor(theme.backgroundColor)
    }

    private fun insertProducts(categoryData: ProductData) {
        vm.insertProduct(categoryData)
    }

    private fun updateProducts(title: String, checked: Boolean, id: Int) {
        vm.updateProduct(title, checked, id)
    }

    private fun delete(categoryData: ProductData) {
        vm.delete(categoryData)
    }

    private fun getProducts(category_id: Int) {
        vm.getProduct(category_id).collectLA(lifecycleScope) {
            if (!start)
                if (it.isNotEmpty()) {
                    binding.listProducts.visibility = View.VISIBLE
                    binding.empty.visibility = View.GONE
                    productData?.clear()
                    productData?.addAll(it)
                    productData?.let { it1 ->
                        mAdapter?.setData(listOf())
                        val data = it.sortedBy {
                            it.checked
                        }
                        mAdapter?.setData(data)
                        start = true
                    }
                } else {
                    binding.listProducts.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                hideKeyboard()
                finish()
            }
            binding.addProduct -> {
                val productName = binding.enterCategoryName.text.toString()
                if (productName.isNotEmpty() && categoryId != -1) {
                    val product = ProductData(0, productName, categoryId, false)
                    insertProducts(product)
                    mAdapter?.dataProduct?.add(product)
                    mAdapter?.dataProduct?.size?.let {
                        mAdapter?.notifyItemInserted(it)
                        binding.listProducts.scrollToPosition(it - 1)
                    }
                    binding.enterCategoryName.text?.clear()
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onItemClick(data: ProductData, position: Int, type: Int, binding: ItemProductBinding) {
        when (type) {
            1 -> {
                val menuBuilder = MenuBuilder(binding.root.context)
                val inflater = MenuInflater(requireContext())
                inflater.inflate(R.menu.menu_edit, menuBuilder)
                val optionsMenu = MenuPopupHelper(requireContext(), menuBuilder, binding.root)
                var state = 0
                optionsMenu.setForceShowIcon(true)
                optionsMenu.show()
                optionsMenu.setOnDismissListener {
                    if (state == 0)
                        mAdapter?.notifyItemChanged(position)
                }
                menuBuilder.setCallback(object : MenuBuilder.Callback {
                    override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menu_edit -> {
                                state = 1
                                val dialog = AddCategoryDialog(binding.root.context, getString(R.string.add_category), getString(R.string.edit), data.title)
                                dialog.setOnCancelClick {
                                    dialog.dismiss()
                                }
                                dialog.setOnDismissListener {
                                    mAdapter?.notifyItemChanged(position)
                                }
                                dialog.setOnSubmitClick { type, text ->
                                    when (type) {
                                        1 -> {
                                            updateProducts(text, !data.checked, data.id)
                                            dialog.dismiss()
                                            mAdapter?.dataProduct?.get(position)?.title = text
                                            mAdapter?.notifyItemChanged(position)
                                        }
                                        2 -> {
                                            snackBar(text)
                                        }
                                    }
                                }
                                dialog.show()
                            }
                            R.id.menu_delete -> {
                                state = 2
                                val dialogDeleteProducts = DialogDeleteCategory(binding.root.context, getString(R.string.delete), getString(R.string.delete_category_message))
                                dialogDeleteProducts.setOnDismissListener {
                                    mAdapter?.notifyItemChanged(position)
                                }
                                dialogDeleteProducts.setOnCancelClick {
                                    dialogDeleteProducts.dismiss()
                                    mAdapter?.notifyItemChanged(position)

                                }
                                dialogDeleteProducts.setOnSubmitClick {
                                    dialogDeleteProducts.dismiss()
                                    mAdapter?.notifyItemChanged(position)
                                    delete(data)
                                    mAdapter?.dataProduct?.removeAt(position)
                                    mAdapter?.notifyItemRemoved(position)
                                }
                                dialogDeleteProducts.show()
                            }
                            R.id.menu_cancel -> {
                                optionsMenu.dismiss()
                            }

                        }
                        onCreateLanguage(languageManager.currentLanguage)
                        return true
                    }

                    override fun onMenuModeChange(menu: MenuBuilder) {

                    }
                })
            }
            2 -> {
                if (!data.checked) {
                    mAdapter?.dataProduct?.let {
                        mAdapter?.notifyItemMoved(position, it.size - 1)
                        val item = it.removeAt(position)
                        it.add(item)
                        mAdapter?.notifyItemChanged(it.size - 1)
                    }
                } else {

                    var lastUncheckedPosition = -1
                    for (i in 0 until mAdapter?.dataProduct?.size!!){
                        lg("$i->${mAdapter?.dataProduct!![i].checked}")
                        if (mAdapter?.dataProduct!![i].checked) {
                            lastUncheckedPosition = i
                            break
                        }
                    }
                    mAdapter?.dataProduct?.let {
                        if (lastUncheckedPosition >= 0) {
                            mAdapter?.notifyItemMoved(position, lastUncheckedPosition)
                            val item = it.removeAt(position)
                            it.add(lastUncheckedPosition, item)
                            mAdapter?.notifyItemChanged(lastUncheckedPosition)
                            lastUncheckedPosition = -1
                        }
                    }

                }
                data.checked = !data.checked
                updateProducts(data.title, data.checked, data.id)

            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let { mItemTouchHelper?.startDrag(it) };
    }

    override fun onNoteListChanged(customers: List<Any?>?) {

    }
}