package uz.bozorliq.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.bozorliq.R
import uz.bozorliq.databinding.ItemProductBinding
import uz.bozorliq.model.product.ProductData
import uz.bozorliq.utils.*
import uz.bozorliq.utils.theme.ClassicTheme
import uz.bozorliq.utils.theme.ThemeManager
import java.util.*


class ProductsAdapter(private val clickListener: OnItemClickListener, private val dragListener: OnStartDragListener, private val listChangedListener: OnCustomerListChangedListener) : RecyclerView.Adapter<ProductsAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    var dataProduct = mutableListOf<ProductData>()
    var next: Int? = null

    fun setData(newData: List<ProductData>) {
        val diffUtil = MyDiffUtil(dataProduct, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        dataProduct = newData.toMutableList()
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        lg("onItemMove->$fromPosition $toPosition")
        Collections.swap(dataProduct, fromPosition, toPosition)
        listChangedListener.onNoteListChanged(dataProduct)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {}
    interface OnItemClickListener {
        fun onItemClick(data: ProductData, position: Int, type: Int, binding: ItemProductBinding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemProductBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)// DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_all_notification, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataProduct[position])
    }

    override fun getItemCount(): Int = dataProduct.size

    inner class MyViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(data: ProductData) {
            binding.nameProduct.text = data.title[0].uppercase() + data.title.substring(1).lowercase()
//            binding.itemBack.setBackgroundColor(binding.root.context.getColor(R.color.back_color))

//            val theme = ThemeManager(binding.root.context).currentTheme
//            binding.itemBack.setBackgroundColor(theme.miniActionTextColor)

            val theme = ThemeManager(binding.root.context).currentTheme
            if (theme.id == ClassicTheme().id) {
                binding.itemBack.setBackgroundColor(binding.root.context.getColor(R.color.back_color))
            } else {
                binding.itemBack.setBackgroundColor(theme.miniActionTextColor)
            }

            if (data.checked) {
                binding.lineStrike.visibility = View.VISIBLE
                binding.checkbox.isChecked = true
            } else {
                binding.lineStrike.visibility = View.GONE
                binding.checkbox.isChecked = false
            }
            binding.itemBack.setOnLongClickListener {
                binding.itemBack.setBackgroundColor(binding.root.context.getColor(R.color.clicked_back_color))
                clickListener.onItemClick(data, bindingAdapterPosition, 1, binding)
                true
            }
            binding.handle.setOnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
            binding.checkbox.setOnClickListener {
//                data.checked = !data.checked
                clickListener.onItemClick(data, bindingAdapterPosition, 2, binding)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(itemView.context.getColor(R.color.unstable_back_color))
            itemView.findViewById<TextView>(R.id.name_product).setTextColor(itemView.context.getColor(R.color.black))
//            }
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(itemView.context.getColor(R.color.back_color))
            val theme = ThemeManager(binding.root.context).currentTheme
            if (theme.id == ClassicTheme().id) {
                binding.itemBack.setBackgroundColor(binding.root.context.getColor(R.color.back_color))
            } else {
                binding.itemBack.setBackgroundColor(theme.miniActionTextColor)
            }
            itemView.findViewById<TextView>(R.id.name_product).setTextColor(itemView.context.getColor(R.color.stable_color))
        }
    }
}
