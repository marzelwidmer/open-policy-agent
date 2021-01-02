# Open Policy Agent (OPA)
OPA Documentation you can find here: https://www.openpolicyagent.org/docs/latest/ 


In OPA, there are three inputs into the decision-making process: (source: https://www.redhat.com/en/blog/open-policy-agent-part-i-—-introduction)
## Data
`Data` is a set of facts about the outside world that `OPA` refers to while making a decision.
For example, when controlling access based on the access control list, the data would be a list of users along with the permissions they were granted. 
Another example: when deciding where to place the next pod on the Kubernetes cluster, the data would be a list of Kubernetes nodes and their currently available capacity. 
Note that data may change over time and OPA caches its latest state in memory. 
The data must be provided to OPA in the JSON format.

## Query 
`Query` Input triggers the decision computation. It specifies the question that OPA should decide upon. 
The query input must be formatted as JSON. For instance, for the question `“Is user Alice allowed to invoke GET /protected/resource?”` 
the query input would contain parameters: `Alice`, `GET`, and `/protected/resource`

## Policy 
`Policy` specifies the computational logic that for the given data and query input yields a policy decision aka query result. 
The computational logic is described as a set of policy rules in the `OPA’s custom policy language called Rego`. 
Note that OPA don’t come with any pre-defined policies. 
OPA is a policy engine that is able to interpret a policy, however, in order to make use of it you have to create a policy yourself and provide it to OPA.


# Install with brew
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
http POST :8181/v1/data/keepcalm/policy/allow <<<'{ "input": { "user": "alice", "access": "write" } }'

or 

http POST :8181/v1/data/keepcalm/policy/allow @acl/input.json
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

## Ask OPA Policy
Ask `OPA` policy `whocan` read.
```bash
http POST :8181/v1/data/keepcalm/policy/whocan <<<'{ "input": { "access": "read" } }'

HTTP/1.1 200 OK
Content-Length: 26
Content-Type: application/json
Date: Sun, 06 Dec 2020 15:11:23 GMT

{
    "result": [
        "alice",
        "bob"
    ]
}
```
Ask `OPA` policy `whocan` write.

```bash
http POST :8181/v1/data/keepcalm/policy/whocan <<<'{ "input": { "access": "write" } }'

HTTP/1.1 200 OK
Content-Length: 20
Content-Type: application/json
Date: Sun, 06 Dec 2020 15:14:01 GMT

{
    "result": [
        "alice"
    ]
}
```
 


# Minikube
## Install with skaffold
```bash
skaffold run
```
## Search POD
```bash
k get po -l 'appGroup in (opa)' -n default

NAME                           READY   STATUS    RESTARTS   AGE
opa-service-79fd6b49dc-dx5q8   1/1     Running   0          2m46s
```
## Forward Port 
```bash
k port-forward po/opa-service-79fd6b49dc-dx5q8 8181:8181
Forwarding from 127.0.0.1:8181 -> 8181
Forwarding from [::1]:8181 -> 8181
```


## Install with skaffold on Openshift
```bash
skaffold run -p openshift
```

