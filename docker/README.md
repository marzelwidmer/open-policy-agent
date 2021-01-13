








```bash
docker run -it -p 8181:8181 openpolicyagent/opa \
    run --server --log-level debug
```

```bash
docker run -it -p 8181:8181 \
  -v $PWD:/example openpolicyagent/opa \
  eval -d /example 'data.example.greeting'
```
