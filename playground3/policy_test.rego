package authz.employee

# 1. Employees can access their own salary
# 2. Employees can assess the salary of people the manage

test_allow_bob_have_access_of_bob_salary {
    allow with input as {"method": "GET", "user": "bob", "path": ["salary", "bob"]}
}

test_allow_fred_have_access_of_fred_salary {
    allow with input as {"method": "GET", "user": "fred", "path": ["salary", "fred"]}
}


# test_allow_alice_have_access_of_fred_salary {
#     allow with input as {"method": "GET", "user": "alice", "path": ["salary", "fred"]}
# }
