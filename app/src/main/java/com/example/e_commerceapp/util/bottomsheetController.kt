package com.example.e_commerceapp.util

//fun Fragment.setupResetPasswordBottomSheet(
//    onRecoverClick: (String) -> Unit,
//    onSubmit: (String, BottomSheetDialog) -> Unit
//) {
//    val dialog = BottomSheetDialog(requireContext())
//    val view = layoutInflater.inflate(R.layout.fragment_reset_password_bottom_sheet, null)
//    dialog.setContentView(view)
//    dialog.show()
//
//    view.apply {
//        val email =
//            findViewById<EditText>(R.id.edEmailAddressInResetPassword).text.toString().trim()
//        findViewById<Button>(R.id.recoveryButton).setOnClickListener {
//            Log.d("zaza check email", "click")
//            onRecoverClick(email)
//            onSubmit(email, dialog)
//        }
//    }
//}