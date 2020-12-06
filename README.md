# Open Policy Agent (OPA)
See also :https://www.openpolicyagent.org/docs/latest/ 

```
brew install opa
```

# Test 
For verbose mode `opa test -v  .`
``` 
opa test .
```
Output as `JSON`
``` 
 opa test --format=json -v .
 ```

See also https://www.openpolicyagent.org/docs/latest/policy-testing/ 




# OPA Server
Start the `OPA` server with the following command:
``` 
opa run --server
```

`OPA` service is now up and listening on port `8181`

`Upload` the `ACL` file `keepcalm-acl.json` into `OPA` using the following curl command:
### Httpie
```bash
http PUT :8181/v1/data/keepcalm/acl @acl/keepcalm-acl.json
```
### Curl
```bash 
curl -X PUT http://localhost:8181/v1/data/keepcalm/acl --data-binary @acl/keepcalm-acl.json
````

`Upload` the `policy` file `keepcalm-policy.rego` into `OPA` by issuing:
### Httpie
```bash
http PUT :8181/v1/policies/keepcalm @acl/keepcalm-policy.rego
```
### Curl
```bash
curl -X PUT http://localhost:8181/v1/policies/keepcalm --data-binary @acl/keepcalm-policy.rego

```

Let’s ask OPA whether the user alice can invoke a write operation on our application:
### Httpie
```bash
http POSt :8181/v1/data/keepcalm/policy/allow <<<'{ "input": { "user": "alice", "access": "write" } }'

or 

http POSt :8181/v1/data/keepcalm/policy/allow @acl/input.json
```

### Curl
```bash
curl -X POST http://localhost:8181/v1/data/keepcalm/policy/allow \
        --data-binary '{ "input": { "user": "alice", "access": "write" } }' \
        | jq
```
The result should be `true`
``` 
 % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    66  100    15  100    51  15000  51000 --:--:-- --:--:-- --:--:-- 66000
{
  "result": true
}
```











