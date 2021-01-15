package authz.employee

# 1. Employees can access their own salary
# 2. Employees can assess the salary of people the manage

default allow = false

allow = true {
	input.method = "GET"
    input.path = ["salary", employee_id]
    input.user = employee_id
}

allow = true {
    input.method = "GET"
    input.path = ["salary", employee_id]
    managers[employee_id][input.user]
}


# Data (normally it will loaded to OPA or pass as input like a token)
managers = {
    "bob": {"alice", "fred"},
    "alice": {"fred"}
}
