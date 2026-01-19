package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.taufiqrahman.reviewratings.BarLabels
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.RatingsAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.Ratings
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.RatingsAndReviewViewModel
import kotlinx.android.synthetic.main.activity_rating_and_review.*
import kotlinx.android.synthetic.main.activity_rating_and_review.progressBar
import kotlinx.android.synthetic.main.activity_tyre_service.*
import kotlinx.android.synthetic.main.header_title.*
import java.util.*

class RatingAndReviewActivity : AppCompatActivity(), RatingsAdapter.ICustomListListener {
    lateinit var mContext: Context
    var colors = intArrayOf(
        Color.parseColor("#39BE6A"),
        Color.parseColor("#39BE6A"),
        Color.parseColor("#39BE6A"),
        Color.parseColor("#39BE6A"),
        Color.parseColor("#39BE6A")
    )


    /**
     * View model
     */
    lateinit var ratingsAndReviewViewModel: RatingsAndReviewViewModel


    lateinit var ratingsAdapter: RatingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_and_review)
        mContext = this@RatingAndReviewActivity
        initView()

        clickListener()
    }

    /**
     * Clicck Events
     */
    private fun clickListener() {

    }


    /**
     * Init all views
     */
    private fun initView() {
        tvTitle.text = getString(R.string.rating_and_review)
        iv_back.setOnClickListener(View.OnClickListener { finish() })

        rvReview.layoutManager = LinearLayoutManager(mContext)
        rvReview.setHasFixedSize(true)

        ratingsAndReviewViewModel =
            ViewModelProvider(this).get(RatingsAndReviewViewModel::class.java)
        ratingsAndReviewViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        ratingsAndReviewViewModel.successMessageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                    intent.putExtra(IntentKeyEnum.IS_REPLY_ADDED.name, "")
                    startActivity(intent)
                    finish()

                }
            })

        ratingsAndReviewViewModel.ratingListLiveData.observe(
            this,
            androidx.lifecycle.Observer { ratingList ->
                progressBar.visibility = View.GONE

                ratingsAdapter = RatingsAdapter(mContext, this)
                rvReview.adapter = ratingsAdapter
                ratingsAdapter.setList(ratingList)
            })

        ratingsAndReviewViewModel.reviewsLiveData.observe(
            this,
            androidx.lifecycle.Observer { reviews ->
                tvAvgRating.text = reviews.average

                if (reviews.total.isNotEmpty()) {
                    tvNumRatings.text = resources.getString(R.string.total) + reviews.total
                }



                if (!reviews.average.isNullOrEmpty()) {
                    ratingBar.rating = reviews.average.toFloat()
                } else {
                    ratingBar.rating = 0f
                }


                var rate1 = 0
                if (reviews.rate_1.isNotEmpty()) {
                    rate1 = reviews.rate_1.toInt()
                }

                var rate2 = 0
                if (reviews.rate_2.isNotEmpty()) {
                    rate2 = reviews.rate_2.toInt()
                }

                var rate3 = 0
                if (reviews.rate_3.isNotEmpty()) {
                    rate3 = reviews.rate_3.toInt()
                }
                var rate4 = 0
                if (reviews.rate_4.isNotEmpty()) {
                    rate4 = reviews.rate_4.toInt()
                }

                var rate5 = 0
                if (reviews.rate_5.isNotEmpty()) {
                    rate5 = reviews.rate_5.toInt()
                }


                var raters = intArrayOf(
                    rate5,
                    rate4,
                    rate3,
                    rate2,
                    rate1,
                )

                rating_reviews.createRatingBars(5, BarLabels.STYPE3, colors, raters)

            })

        ratingsAndReviewViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()

        })


    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        val productList = ProductList()
        productList.seller_id = Helper().getLoginData(mContext).id
        productList.seller_company_id = ratingsAndReviewViewModel.companyId

        ratingsAndReviewViewModel.apiGetReview(productList)
    }

    override fun onReplyOfRating(ratings: Ratings, reply: String) {

        if (reply.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE

            val ratingsForSubmit = Ratings()
            ratingsForSubmit.rating_id = ratings.id
            ratingsForSubmit.seller_reply = reply

            ratingsAndReviewViewModel.apiSendReply(ratingsForSubmit)

        } else {
            Toast.makeText(mContext, resources.getString(R.string.enter_reply), Toast.LENGTH_SHORT)
                .show()
        }


    }
}