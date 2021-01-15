package authz


allow {
    input.path == ["users"]
    input.method == "POST"
}

allow {
    some profile_id
    input.path = ["users", profile_id]
    input.method == "GET"
    profile_id == input.user_id
}

allow {
    input._embedded.partners[_].role == "FAVOR"
}
