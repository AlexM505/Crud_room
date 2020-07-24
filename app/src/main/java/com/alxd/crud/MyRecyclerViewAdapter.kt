package com.alxd.crud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alxd.crud.databinding.ListItemBinding
import com.alxd.crud.db.Subscriber

class MyRecyclerViewAdapter(private val clickListener: (Subscriber)->Unit) : RecyclerView.Adapter<MyViewHolder>() {

    private val subscribers = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  subscribers.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribers[position],clickListener)
    }

    fun setList(subscribersList: List<Subscriber>){
        subscribers.clear()
        subscribers.addAll(subscribersList)
    }
}

class MyViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber:Subscriber, clickListener: (Subscriber)->Unit){
        binding.tvNameItem.text = subscriber.name
        binding.tvEmailItem.text = subscriber.email
        binding.itemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }
}