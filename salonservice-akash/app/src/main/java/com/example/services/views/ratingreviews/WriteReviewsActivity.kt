package com.example.services.views.ratingreviews

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.RatingBar
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.callbacks.ChoiceCallBack
import com.example.services.common.UtilsFunctions
import com.example.services.model.ratnigreviews.RatingReviewListInput
import com.example.services.utils.BaseActivity
import com.example.services.utils.DialogClass
import com.example.services.viewmodels.ratingreviews.RatingReviewsViewModel
import com.google.gson.JsonObject
import com.uniongoods.adapters.AddRatingReviewsListAdapter
import com.uniongoods.adapters.ReviewsImagesListAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.services.databinding.ActivityWriteReviewsBinding

import kotlin.collections.ArrayList

class WriteReviewsActivity : BaseActivity(), ChoiceCallBack, RatingBar.OnRatingBarChangeListener {
    lateinit var reviewsBinding: ActivityWriteReviewsBinding
    lateinit var reviewsViewModel: RatingReviewsViewModel
    var reviewsAdapter: AddRatingReviewsListAdapter? = null
    var cartObject = JsonObject()
    var count = 0
    var orderId = ""
    private val mJsonObject = JsonObject()
    private val RESULT_LOAD_IMAGE = 100
    private val CAMERA_REQUEST = 1888
    private var profileImage = ""
    var mLoadMoreViewCheck = true
    var imagesAdapter: ReviewsImagesListAdapter? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    //var suborders: ArrayList<OrdersDetailResponse.Suborders>? = null
    var imagesPaths = ArrayList<String>()
    val ratingData = RatingReviewListInput()
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()

    override fun getLayoutId(): Int {
        return R.layout.activity_write_reviews
    }

    override fun initViews() {

        reviewsBinding = viewDataBinding as ActivityWriteReviewsBinding
        reviewsViewModel = ViewModelProviders.of(this).get(RatingReviewsViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(this)
        reviewsBinding.commonToolBar.imgRight.visibility = View.GONE
        reviewsBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        reviewsBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.write_review)
        reviewsBinding.reviewsViewModel = reviewsViewModel

        initRecyclerView()
        reviewsBinding.rbRatings.onRatingBarChangeListener = this
        reviewsViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "btnSubmit" -> {
                        if (UtilsFunctions.isNetworkConnected()) {
                            /* startProgressDialog()

                             //  ratingData.ratingData?.add(rating)
                             if (ratingData.ratingData.size > 0) {
                                 val last = ratingData.ratingData.size - 1
                                 val empRating = EmpRatingData()
                                 empRating.rating = ratingData.ratingData[last].rating
                                 empRating.review = ratingData.ratingData[last].review
                                 empRating.empId = ratingData.ratingData[last].serviceId
                                 ratingData.empRatingData.add(empRating)
                                 ratingData.ratingData.removeAt(last)
                             }
                             reviewsViewModel.addRatings(ratingData)*/
                        }

                    }
                    "imgProduct" -> {
                        if (checkAndRequestPermissions()) {
                            confirmationDialog =
                                mDialogClass.setUploadConfirmationDialog(this, this, "gallery")
                        }

                    }
                    /*"rb_ratings" -> {
                        val msg = reviewsBinding.rbRatings.rating.toString()
                        reviewsBinding.tvRating.setText(msg + "/5")
                    }*/
                }
            })
        )

    }

    private fun initRecyclerView() {
        imagesAdapter = ReviewsImagesListAdapter(this, imagesPaths, this)
        reviewsBinding.rvReviews.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        reviewsBinding.rvReviews.layoutManager = linearLayoutManager
        reviewsBinding.rvReviews.adapter = imagesAdapter
        reviewsBinding.rvReviews.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    override fun photoFromCamera(mKey: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(this, packageName + ".fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //currentPhotoPath = File(baseActivity?.cacheDir, fileName)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            profileImage = absolutePath
        }
    }

    override fun photoFromGallery(mKey: String) {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    override fun videoFromCamera(mKey: String) {
        //("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun videoFromGallery(mKey: String) {
        //("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            profileImage = picturePath
            setImage(picturePath)
            cursor.close()
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK /*&& null != data*/) {
            setImage(profileImage)            // val extras = data!!.extras
            // val imageBitmap = extras!!.get("data") as Bitmap
            //getImageUri(imageBitmap)
        }

    }

    private fun setImage(path: String) {
        imagesPaths.add(path)
        if (imagesPaths.size > 4) {
            reviewsBinding.imgProduct.visibility = View.GONE
        } else {
            reviewsBinding.imgProduct.visibility = View.VISIBLE
        }
        imagesAdapter?.notifyDataSetChanged()
        /*Glide.with(this)
            .load(path)
            .placeholder(R.drawable.user)
            .into(profileBinding.imgProfile)*/
    }

    fun removeImage(position: Int) {
        imagesPaths.removeAt(position)
        if (imagesPaths.size > 4) {
            reviewsBinding.imgProduct.visibility = View.GONE
        } else {
            reviewsBinding.imgProduct.visibility = View.VISIBLE
        }
        imagesAdapter?.notifyDataSetChanged()
        /*Glide.with(this)
            .load(path)
            .placeholder(R.drawable.user)
            .into(profileBinding.imgProfile)*/
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        reviewsBinding.tvRating.setText("$rating" + "/5")
    }

}
