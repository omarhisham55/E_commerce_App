package com.example.e_commerceapp.data

data class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var imgPath: String = "",
) {
    constructor() : this(firstName = "", lastName = "", email = "", imgPath = "")

    fun fromJson(json: Map<String, Any>): User = User(
        firstName = json["firstName"].toString(),
        lastName = json["lastName"].toString(),
        email = json["email"].toString(),
        imgPath = json["imgPath"].toString(),
    )
}
