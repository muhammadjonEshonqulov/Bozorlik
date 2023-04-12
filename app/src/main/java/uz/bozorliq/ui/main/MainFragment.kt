package uz.bozorliq.ui.main

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uz.bozorliq.R
import uz.bozorliq.adapters.CategoryAdapter
import uz.bozorliq.base.AddCategoryDialog
import uz.bozorliq.base.BaseFragment
import uz.bozorliq.base.DialogDeleteCategory
import uz.bozorliq.databinding.FragmentMainBinding
import uz.bozorliq.databinding.HeaderLayoutBinding
import uz.bozorliq.databinding.ItemCategoryBinding
import uz.bozorliq.model.category.CategoryData
import uz.bozorliq.utils.*
import uz.bozorliq.utils.theme.ClassicTheme
import uz.bozorliq.utils.theme.NightTheme
import uz.bozorliq.utils.theme.Theme
import uz.smartavtodrom.utils.collectLA
import java.util.*


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate), NavigationView.OnNavigationItemSelectedListener, OnStartDragListener, OnCustomerListChangedListener, CategoryAdapter.OnItemClickListener, OnClickListener {

    private val vm: MainViewModel by viewModels()
    private var categoryData: ArrayList<CategoryData>? = null


    private var mAdapter: CategoryAdapter? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    lateinit var bindNav: HeaderLayoutBinding


    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        bindNav = HeaderLayoutBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.header_layout, null, false))
        binding.navView.addHeaderView(bindNav.root)
        start = false
        getCategory()
        binding.menu.setOnClickListener(this)
        binding.addCategory.setOnClickListener(this)
        bindNav.imgNightMode.setOnClickListener(this)
        binding.navView.setNavigationItemSelectedListener(this)

        if (categoryData == null) {
            categoryData = arrayListOf()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.drawer.closeDrawer(GravityCompat.START)
    }

    private fun setupRecyclerView() {

        binding.listCategory.setHasFixedSize(true)
        binding.listCategory.layoutManager = LinearLayoutManager(binding.root.context)

        mAdapter = CategoryAdapter(this, this, this)
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(mAdapter!!)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(binding.listCategory)
        binding.listCategory.adapter = mAdapter
    }

    private fun insertCategory(categoryData: CategoryData) {
        vm.insertCategory(categoryData)
    }

    private fun updateCategory(title: String, category_id: Int) {
        vm.updateCategory(title, category_id)
    }

    private fun delete(categoryData: CategoryData) {
        vm.delete(categoryData)
    }

    var start = false


    private fun getCategory() {
        vm.getCategory.collectLA(lifecycleScope) {
            if (!start)
                if (it.isNotEmpty()) {
                    binding.listCategory.visibility = View.VISIBLE
                    binding.empty.visibility = View.GONE
                    categoryData?.clear()
                    categoryData?.addAll(it)
                    categoryData?.let { it1 ->
                        lg("data-> getCategory" + it1)
                        mAdapter?.setData(listOf())
                        mAdapter?.setData(it1)
                        start = true
                    }
                } else {
                    binding.listCategory.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                }
        }
    }

    override fun onCreateTheme(theme: Theme) {
        super.onCreateTheme(theme)
//        bindNav = HeaderLayoutBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.header_layout, null, false))

//        binding.navView.addHeaderView(bindNav.root)

        binding.actionBar.setBackgroundColor(theme.colorPrimary)
        binding.navView.setBackgroundColor(theme.backgroundColor)
        binding.navView.itemTextColor = ColorStateList.valueOf(theme.textColor)
        binding.drawer.setBackgroundColor(theme.backgroundColor)
        bindNav.backHeader.setBackgroundColor(theme.colorPrimary)

        when (theme.id) {
            ClassicTheme().id -> {
                bindNav.imgNightMode.setImageResource(R.drawable.ic_moonstars)
            }
            NightTheme().id -> {
                bindNav.imgNightMode.setImageResource(R.drawable.ic_sun)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addCategory -> {
                val dialog = AddCategoryDialog(binding.root.context, getString(R.string.add), getString(R.string.add_category))
                dialog.setOnCancelClick {
                    dialog.dismiss()
                }
                dialog.setOnSubmitClick { type, text ->
//                    dialog.dismiss()
                    when (type) {
                        1 -> {
                            val category = CategoryData(0, text)
                            insertCategory(category)
                            dialog.dismiss()
                            mAdapter?.dataProduct?.add(category)
                            mAdapter?.dataProduct?.size?.let {
                                mAdapter?.notifyItemInserted(it)
                                binding.listCategory.scrollToPosition(it - 1)
                            }
                            // getCategory()
                        }
                        2 -> {
                            snackBar(text)
                        }
                    }
                }
                dialog.show()
            }
            binding.menu -> {
                binding.drawer.openDrawer(GravityCompat.START)
            }
            bindNav.imgNightMode -> {
                switchTheme()
            }
        }
    }

    private fun switchTheme() {
        val prefs = Prefs(binding.root.context)
        val theme = prefs.get(prefs.theme, Theme.CLASSIC_THEME)
        when (theme) {
            Theme.CLASSIC_THEME -> {
                bindNav.imgNightMode.setImageResource(R.drawable.ic_sun)
            }
            Theme.NIGHT_THEME -> {
                bindNav.imgNightMode.setImageResource(R.drawable.ic_moonstars)
            }
        }
        val newTheme = if (theme == Theme.NIGHT_THEME) Theme.CLASSIC_THEME else Theme.NIGHT_THEME
        prefs.save(prefs.theme, newTheme)
        notifyThemeChanged()
//        mAdapter?.dataProduct?.let { mAdapter?.setData(it) }
        mAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("RestrictedApi")
    override fun onItemClick(data: CategoryData, position: Int, type: Int, binding1: ItemCategoryBinding) {
        when (type) {
            1 -> {
                val menuBuilder = MenuBuilder(binding1.root.context)
                val inflater = MenuInflater(requireContext())
                inflater.inflate(R.menu.menu_edit, menuBuilder);
                val optionsMenu = MenuPopupHelper(requireContext(), menuBuilder, binding1.root)
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
                                val dialog = AddCategoryDialog(binding1.root.context, getString(R.string.add_category), getString(R.string.edit), data.title)
                                dialog.setOnCancelClick {
                                    dialog.dismiss()
                                }
                                dialog.setOnDismissListener {
                                    mAdapter?.notifyItemChanged(position)
                                }
                                dialog.setOnSubmitClick { type, text ->
                                    when (type) {
                                        1 -> {
                                            updateCategory(text, data.id)
                                            dialog.dismiss()
                                            mAdapter?.dataProduct?.get(position)?.title = text
                                            mAdapter?.notifyItemChanged(position)
                                        }
                                        2 -> {
                                            snackBar(text)
                                        }
                                    }
                                }
//                                dialog.setOnDismissListener {
//                                    lg("setOnDismissListener")
//                                    getCategory()
//                                }
                                dialog.show()
                            }
                            R.id.menu_delete -> {
                                state = 2
                                val dialogDeleteCategory = DialogDeleteCategory(binding.root.context, getString(R.string.delete), getString(R.string.delete_category_message))
                                dialogDeleteCategory.setOnDismissListener {
                                    mAdapter?.notifyItemChanged(position)
                                }
                                dialogDeleteCategory.setOnCancelClick {
                                    dialogDeleteCategory.dismiss()
                                }
                                dialogDeleteCategory.setOnSubmitClick {
                                    dialogDeleteCategory.dismiss()
                                    mAdapter?.notifyItemChanged(position)
                                    delete(data)
                                    mAdapter?.dataProduct?.removeAt(position)
                                    mAdapter?.notifyItemRemoved(position)
                                    snackBar("Size->"+mAdapter?.dataProduct?.size)
                                    if (mAdapter?.dataProduct?.size == 0) {
                                        binding.listCategory.visibility = View.GONE
                                        binding.empty.visibility = View.VISIBLE
                                    } else {
                                        binding.listCategory.visibility = View.VISIBLE
                                        binding.empty.visibility = View.GONE
                                    }
                                }
                                dialogDeleteCategory.show()
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

//                mAdapter?.dataProduct?.get(position)?.longClicked = !data.longClicked


            }
            2 -> {
                findNavController().navigate(R.id.action_mainFragment_to_student_productFragment, bundleOf("category_name" to data.title, "category_id" to data.id))
            }

        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let { mItemTouchHelper?.startDrag(it) };
    }

    override fun onNoteListChanged(customers: List<Any?>?) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                findNavController().navigateSafe(R.id.action_mainFragment_to_settingsFragment)
            }
            R.id.nav_feedback -> {
//                findNavController().navigateSafe(R.id.action_mainFragment_to_feedbackFragment)
            }
            R.id.nav_theme -> {
                findNavController().navigateSafe(R.id.action_mainFragment_to_themeFragment)
            }
            R.id.nav_language -> {
                findNavController().navigateSafe(R.id.action_mainFragment_to_languageFragment)
            }
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }
}