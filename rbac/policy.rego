package authz.rbac

default allow = false

# RBAC
allow {
	input.user.role = "admin"
}
