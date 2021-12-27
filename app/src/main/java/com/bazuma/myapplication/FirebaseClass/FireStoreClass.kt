package com.bazuma.myapplication.FirebaseClass

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.models.User
import com.bazuma.myapplication.ui.activities.*
import com.bazuma.myapplication.ui.ui.fragments.ProductsFragment
import com.bazuma.myapplication.utilis.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        //"users is the collection name
        //if the collection already created then it will not create the same
        mFirestore.collection(Constants.USERS)

            //document id for user field.here the document is the user id
            .document(userInfo.id)

            //here user info is the fiels and setOption is set to merge.
            //used if we want to merge later
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the use",
                    e
                )

            }

    }

    fun getCurrentUserID(): String {
        //instance of current user in FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign currentUserID if it is not null or it will be empty
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUsersDetails(activity: Activity) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                //We have received Document snapshot converted into user data model object
                val user = document.toObject(User::class.java)!!

                //We Using sharedPreferences when we want to save data in it
                //into our phones and requires sharedPreferences object to do the work
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPPAL_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                //We are required to use an sharedPreferences editor object
                //Used to edit our MYSHOPPAL_PREFERENCES
                //We required to have a key(Constants.LOGGED_IN_USERNAME)
                // value pair("${user.firstName}  ${user.lastName}")
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName}  ${user.lastName}"
                )
                //key:value->logged_in_username:Ayubu Mohamed
                editor.apply()
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingActivity -> {
                        activity.userDetailsSuccess(user)
                    }

                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imageType:String) {
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    activity,
                    imageFileURI
                )
            )
            sRef.putFile(imageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    // The image upload is success
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    // Get the downloadable url from the task snapshot
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())


                            // Here call a function of base activity for transferring the result to it.
                            when (activity) {
                                is UsersProfileActivity -> {
                                    activity.imageUploadSuccess(uri.toString())
                                }
                                is AddProductActivity -> {
                                    activity.imageProductUploadSuccess(uri.toString())
                                }
                            }
                            // END
                        }
                }
                .addOnFailureListener { exception ->

                    // Hide the progress dialog if there is any error. And print the error in log.
                    when (activity) {
                        is UsersProfileActivity -> {
                            activity.hideProgressDialog()
                        }
                        is AddProductActivity -> {
                            activity.hideProgressDialog()
                        }

                    }

                    Log.e(
                        activity.javaClass.simpleName,
                        exception.message,
                        exception
                    )
                }
        }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
            // Collection Name
            mFirestore.collection(Constants.USERS)
                // Document ID against which the data to be updated. Here the document id is the current logged in user id.
                .document(getCurrentUserID())
                // A HashMap of fields which are to be updated.
                .update(userHashMap)
                .addOnSuccessListener {

                    // Notify the success result.
                    when (activity) {
                        is UsersProfileActivity -> {
                            // Call a function of base activity for transferring the result to it.
                            activity.userProfileUpdateSuccess()
                        }
                    }
                }
                .addOnFailureListener { e ->

                    when (activity) {
                        is UsersProfileActivity -> {
                            // Hide the progress dialog if there is any error. And print the error in log.
                            activity.hideProgressDialog()
                        }
                    }

                    Log.e(
                        activity.javaClass.simpleName,
                        "Error while updating the user details.",
                        e
                    )
                }
        }

    fun uploadProductsDetails(activity: AddProductActivity, productInfo: Product) {
        mFirestore.collection(Constants.PRODUCT)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
               activity.ProductUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while upload product",
                    e
                )

            }

    }

    fun getProductsDetails(fragment: Fragment){
        mFirestore.collection(Constants.PRODUCT)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
               Log.e("Products lists",document.documents.toString())
               val productsList : ArrayList<Product> =ArrayList()
                for (x in document.documents){
                    var product = x.toObject(Product::class.java)
                    product!!.my_product_id=x.id

                    productsList.add(product)
                }
                when(fragment){
                    is ProductsFragment->{
                        fragment.successProductListFromFirestore(productsList)
                    }
                }
            }
    }
}



