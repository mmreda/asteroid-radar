package com.udacity.asteroidradar.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.main.MainViewModel.NasaApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter(value = [ "pictureOfDay", "status" ])
fun bindImageView(imageView: ImageView, pictureOfDay: PictureOfDay?, status: NasaApiStatus) {

    if(pictureOfDay?.mediaType == "image") {
        Picasso.get()
            .load(pictureOfDay.url)
            .into(imageView)
        imageView.contentDescription = imageView.context.getString(
            R.string.nasa_picture_of_day_content_description_format,
            pictureOfDay.title)
    } else {
        imageView.contentDescription = imageView.context.getString(
            R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }

    if (status == NasaApiStatus.ERROR) {
        imageView.setImageResource(R.drawable.ic_broken_image)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
     val adapter =recyclerView.adapter as AsteroidAdapter
    adapter.submitList(asteroidList)
}

@BindingAdapter("progressBar")
fun statusProgressBar(progressBar: ProgressBar, status: NasaApiStatus) {
    when (status) {
        NasaApiStatus.LOADING ->
            progressBar.visibility = View.VISIBLE

        NasaApiStatus.DONE ->
            progressBar.visibility = View.GONE

        NasaApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
    }
}