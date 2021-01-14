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


# Opa Server
## Install with brew
```bash
brew install opa
```

Start the `OPA` server with the following command:
```bash
opa run --server
```
`OPA` service is now up and listening on port `8181`


## OPA Server Docker Image
```bash
docker run -it -p 8181:8181 openpolicyagent/opa run --server --log-level debug
```



# Test 
For verbose mode `opa test -v  .`
```bash 
opa test .
```
Output as `JSON`
```bash 
opa test --format=json -v .
 ```
See also : [https://www.openpolicyagent.org/docs/latest/policy-testing/](https://www.openpolicyagent.org/docs/latest/policy-testing/ )

 

# Sample Access Control List
Based on : [https://www.redhat.com/en/blog/open-policy-agent-part-i-—-introduction](https://www.redhat.com/en/blog/open-policy-agent-part-i-—-introduction)

## Data
Access control list specifies which users have access to the application as well as what operations they are allowed to invoke. 
For the purposes of this tutorial, I came up with a simple `ACL` definition:
```json
{
  "alice": [
    "read",
    "write"
  ],
  "bob": [
    "read"
  ]
}
```

`Upload` the `data` file [data](acl/data.json)  into `OPA` using the following `httpie/curl` command:
### Httpie
```bash
http PUT :8181/v1/data/http/authz/acl @acl/data.json
```
### Curl
```bash
curl -X PUT http://localhost:8181/v1/data/http/authz/acl --data-binary @acl/data.json
````


## Input
The query `input`. On each access to the application, we are going to ask `OPA` whether the given access is authorized or not. 
To answer that question, `OPA` needs to know the name of the user that is trying to access the application, and the operation that the user is trying to invoke. 

Here is a sample query input that conveys the two `query` arguments to `OPA` :
```json
{
  "input": {
    "user": "alice",
    "operation": "write"
  }
}
```

## Rego Policy
Create a policy that implements the `ACL` semantics with two rules `allow` and `whocan`: 

The rule `allow` checks whether the user is allowed access according to the `ACL`.
First look up the users' record in `ACL` and check the operation the user is trying to invoke is included on user’s permission list.
Only if there is an `ACL` record for the given user, and the user was granted given access permission, the `allow` rule results to true.

The rule `whocan` takes the operation as the input argument.
For the given operation, `whocan` rule returns a list of all users that are allowed to invoke the given operation.

[policy.rego](acl/policy.rego)

```rego
package http.authz.policy

import data.http.authz.acl
import input

default allow = false

allow {
        access = acl[input.user]
        access[_] == input.access
}

whocan[user] {
        access = acl[user]
        access[_] == input.access
}
```

`Upload` the `policy` file [policy.rego](acl/policy.rego) into `OPA` by issuing:
### Httpie
```bash
http PUT :8181/v1/policies/http/authz @acl/policy.rego
```
### Curl
```bash
curl -X PUT http://localhost:8181/v1/policies/http/authz --data-binary @acl/policy.rego

```

Let’s ask `OPA` whether the `user alice` can invoke a `write operation` on our application:
### Httpie
```bash
http POST :8181/v1/data/http/authz/policy/allow <<<'{ "input": { "user": "alice", "access": "write" } }'
````
or with the [input.json](acl/input.json)
```bash
http POST :8181/v1/data/http/authz/policy/allow @acl/input.json
```
### Curl
```bash
curl -X POST http://localhost:8181/v1/data/http/authz/policy/allow \
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
### Whocan red
Ask `OPA` policy `whocan` read.
```bash
http POST :8181/v1/data/http/authz/policy/whocan <<<'{ "input": { "access": "read" } }'

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
### Whocan write
Ask `OPA` policy `whocan` write.
```bash
http POST :8181/v1/data/http/authz/policy/whocan <<<'{ "input": { "access": "write" } }'

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

