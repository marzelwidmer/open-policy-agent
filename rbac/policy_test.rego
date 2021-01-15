package authz.rbac

test_allow_is_false_by_default{
	 not allow
}

test_allow_if_role_admin {
	allow with input as {"user": {"role": "admin"}}
}

test_allow_if_not_role_admin {
	not allow with input as {"user": {"role": "others"}}
}
