package com.example.services.views.favorite

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.model.CommonModel
import com.example.services.model.cart.CartListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseActivity
import com.example.services.utils.DialogClass
import com.example.services.viewmodels.favorite.FavoriteViewModel
import com.example.services.viewmodels.services.ServicesViewModel
import com.google.gson.JsonObject
import com.example.services.databinding.ActivityFavoriteListBinding
import com.example.services.model.fav.FavListResponse
import com.example.services.utils.DialogssInterface
import com.example.services.viewmodels.home.HomeViewModel
import com.example.services.views.subcategories.ServiceDetailActivity
import com.uniongoods.adapters.FavoriteListAdapter

class FavoriteListActivity : BaseActivity(), DialogssInterface {
    lateinit var favoriteBinding: ActivityFavoriteListBinding
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var servicesViewModel: ServicesViewModel
    var cartList = ArrayList<FavListResponse.Body>()
    var favoriteListAdapter: FavoriteListAdapter? = null
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    var cartObject = JsonObject()
    var pos = 0
    lateinit var homeViewModel: HomeViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_favorite_list
    }

    override fun initViews() {
        favoriteBinding = viewDataBinding as ActivityFavoriteListBinding
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        servicesViewModel = ViewModelProviders.of(this).get(ServicesViewModel::class.java)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        favoriteBinding.commonToolBar.imgRight.visibility = View.GONE
        favoriteBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        favoriteBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.favorite)
        val userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
            //cartViewModel.getcartList(userId)
        }

        homeViewModel.getClearCartRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            SharedPrefClass().putObject(
                                this!!,
                                GlobalConstants.isCartAdded,
                                "false"
                            )
                            cartList.clear()
                            favoriteViewModel.getFavList()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            //fragmentHomeBinding.gvServices.visibility = View.GONE
                        }
                    }
                }
            })

        favoriteViewModel.getFavListRes().observe(this,
            Observer<FavListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            cartList.addAll(response.body!!)
                            if(cartList.size>0){
                                favoriteBinding.rvFavorite.visibility = View.VISIBLE
                                favoriteBinding.tvNoRecord.visibility = View.GONE
                                favoriteBinding.title.visibility = View.GONE
                                initRecyclerView()
                            }else{
                                favoriteBinding.rvFavorite.visibility = View.GONE
                                favoriteBinding.tvNoRecord.visibility = View.VISIBLE
                                favoriteBinding.title.visibility = View.VISIBLE
                            }

                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            favoriteBinding.rvFavorite.visibility = View.GONE
                            favoriteBinding.title.visibility = View.VISIBLE
                            favoriteBinding.tvNoRecord.visibility = View.VISIBLE
                        }
                    }

                } else {
                    favoriteBinding.rvFavorite.visibility = View.GONE
                    favoriteBinding.title.visibility = View.VISIBLE
                    favoriteBinding.tvNoRecord.visibility = View.VISIBLE
                }
            })

        servicesViewModel.addRemovefavRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            //cartViewModel.getcartList(userId)
                            //cartList.removeAt(pos)
                            // favoriteListAdapter?.notifyDataSetChanged()
                            favoriteViewModel.getFavList()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

    }


    private fun initRecyclerView() {
        favoriteListAdapter = FavoriteListAdapter(this, cartList, this)
        // val linearLayoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 1)
        favoriteBinding.rvFavorite.layoutManager = gridLayoutManager
        favoriteBinding.rvFavorite.setHasFixedSize(true)
        // linearLayoutManager.orientation = RecyclerView.VERTICAL
        // favoriteBinding.rvFavorite.layoutManager = linearLayoutManager
        favoriteBinding.rvFavorite.adapter = favoriteListAdapter
        favoriteBinding.rvFavorite.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    fun addRemoveToCart(position: Int) {
        pos = position

        cartObject.addProperty(
            "serviceId", cartList[position].serviceId
        )
        cartObject.addProperty(
            "status", "false"
        )
        confirmationDialog = mDialogClass.setDefaultDialog(
            this,
            this,
            "Remove Fav",
            getString(R.string.warning_remove_fav)
        )
        confirmationDialog?.show()


    }

    fun callServiceDetail(serviceId: String) {
        val intent = Intent(this, ServiceDetailActivity::class.java)
        intent.putExtra("serviceId", serviceId)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun clearCartDialog() {
        confirmationDialog = mDialogClass.setDefaultDialog(
            this,
            this,
            "Clear Cart",
            getString(R.string.warning_clear_cart)
        )
        confirmationDialog?.show()
    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "Remove Fav" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    servicesViewModel.removeFav(cartList[pos].id.toString())
                    startProgressDialog()
                }
            }
            "Clear Cart" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    /* servicesViewModel.removeCart(pos)
                     startProgressDialog()*/
                    homeViewModel.clearCart("clear")
                }

            }
        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            "Remove Fav" -> confirmationDialog?.dismiss()
            "Clear Cart" -> confirmationDialog?.dismiss()
        }
    }

}
