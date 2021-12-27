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
import com.bazuma.myapplication.ui.ui.fragments.DashboardFragment
import com.bazuma.myapplication.ui.ui.fragments.ProductsFragment
import com.bazuma.myapplication.utilis.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYSHOPPAL_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                // Create an instance of the editor which is help us to edit the SharedPreference.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }

                    is SettingActivity -> {
                        // Call a function of base activity for transferring the result to it.
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

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    /**
     * A function to update the user profile data into the database.
     *
     * @param activity The activity is used for identifying the Base activity to which the result is passed.
     * @param userHashMap HashMap of fields which are to be updated.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
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

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
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

    /**
     * A function to make an entry of the user's product in the cloud firestore database.
     */
    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {

        mFireStore.collection(Constants.PRODUCT)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.ProductUpdateSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    /**
     * A function to get the products list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getProductsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCT)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.my_product_id = i.id

                    productsList.add(product)
                }

                when (fragment) {
                    is ProductsFragment -> {
                        fragment.successProductListFromFirestore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.hideProgressDialogForFragment()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }

    /**
     * A function to get the dashboard items list. The list will be an overall items list, not based on the user's id.
     */
    fun getDashboardItemsList(fragment: DashboardFragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCT)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)!!
                    product.my_product_id = i.id
                    productsList.add(product)
                }

                // Pass the success result to the base fragment.
                fragment.successDashboardItemList(productsList)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                fragment.hideProgressDialogForFragment()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productID: String) {
        mFireStore.collection(Constants.PRODUCT)
            .document(productID)
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialogForFragment()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting",
                    e
                )
            }
        fun getUsersDetails(activity: Activity) {
            mFireStore.collection(Constants.USERS)
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

        fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {
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
            mFireStore.collection(Constants.USERS)
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
            mFireStore.collection(Constants.PRODUCT)
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

        fun getProductsDetails(fragment: Fragment) {
            mFireStore.collection(Constants.PRODUCT)
                .whereEqualTo(Constants.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Products lists", document.documents.toString())
                    val productsList: ArrayList<Product> = ArrayList()
                    for (x in document.documents) {
                        var product = x.toObject(Product::class.java)
                        product!!.my_product_id = x.id

                        productsList.add(product)
                    }
                    when (fragment) {
                        is ProductsFragment -> {
                            fragment.successProductListFromFirestore(productsList)
                        }
                    }
                }
        }

        fun getDashboardItemList(fragment: DashboardFragment) {
            mFireStore.collection(Constants.PRODUCT)
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Products lists", document.documents.toString())
                    val productsList: ArrayList<Product> = ArrayList()
                    for (x in document.documents) {
                        var product = x.toObject(Product::class.java)
                        product!!.my_product_id = x.id

                        productsList.add(product)
                    }
                    fragment.successDashboardItemList(productsList)
                }
                .addOnFailureListener { e ->
                    fragment.hideProgressDialogForFragment()
                    Log.e(fragment.javaClass.simpleName,
                        "Error while getting dashboard details",
                        e)

                }
        }
    }
}



