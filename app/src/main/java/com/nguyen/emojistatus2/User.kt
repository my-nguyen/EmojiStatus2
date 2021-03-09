package com.nguyen.emojistatus2

// field names (displayName and emojis) should match exactly the fields in Firebase Cloud Firestore
data class User(val displayName: String="", val emojis: String="")