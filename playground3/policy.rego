package authz.employee

# 1. Employees can access their own salary
# 2. Employees can assess the salary of people the manage

default allow = false

allow = true {
	input.method = "GET"
    input.path = ["salary", employee_id]
    input.user = employee_id
}
