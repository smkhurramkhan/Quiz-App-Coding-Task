package com.example.codingchallange.ui.scorecard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallange.databinding.ItemScoresBinding
import com.example.codingchallange.roomdb.entity.HighScoreEntity
import com.example.codingchallange.ui.scorecard.vh.HighScoreVH
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScoreCardAdapter(
    val context: Context,
    private var scoreList: MutableList<HighScoreEntity>
) : RecyclerView.Adapter<HighScoreVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreVH {
        return HighScoreVH(
            ItemScoresBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    override fun onBindViewHolder(holder: HighScoreVH, position: Int) {
        val mScore = scoreList[position]

        holder.binding.apply {
            username.text = mScore.username
            tvScore.text = "${mScore.score}"
            time.text = convertTimestampToFormattedTime(mScore.time)

        }
    }

    private fun convertTimestampToFormattedTime(timestamp: Long): String {
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return format.format(date)
    }


    fun setList(datList: MutableList<HighScoreEntity>) {
        this.scoreList = datList
        notifyDataSetChanged()

    }
}