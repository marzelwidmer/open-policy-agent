//package ch.keepcalm.demo.authz
//
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.security.access.AccessDecisionVoter
//import org.springframework.security.access.ConfigAttribute
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.web.FilterInvocation
//import org.springframework.web.client.RestTemplate
//import java.util.Objects
//import java.util.Map
//import java.util.stream.Collectors
//
//class OPAVoter(private val uri: String = "http://localhost:8181/v1/data/authz/allow") : AccessDecisionVoter<FilterInvocation> {
//
//    private val log: Logger = LoggerFactory.getLogger(javaClass)
//
//    private val objectMapper = ObjectMapper()
//    private val restTemplate = RestTemplate()
//
//    override fun supports(configAttribute: ConfigAttribute?): Boolean {
//        return true
//    }
//
//    override fun supports(clazz: Class<*>?): Boolean {
//        return FilterInvocation::class.java.isAssignableFrom(clazz)
//    }
//
//    override fun vote(authentication: Authentication?, filterInvocation: FilterInvocation?, mutableCollection: MutableCollection<ConfigAttribute>?): Int {
//
//        val name = authentication!!.name
//        val authorities = authentication.authorities
//            .stream()
//            .map { obj: GrantedAuthority -> obj.authority }
//            .collect(Collectors.toUnmodifiableList())
//        val method = filterInvocation!!.request.method
//        val path: List<String> = filterInvocation.request.requestURI.replace("^/|/$", "").split("/")
//
//        val input = Map.of(
//            "name", name,
//            "authorities", authorities,
//            "method", method,
//            "path", path
//        )
//
//        val requestNode = objectMapper.createObjectNode()
//        requestNode.set<JsonNode>("input", objectMapper.valueToTree(input))
//        log.info(
//            """
//                Authorization request:
//                ${requestNode.toPrettyString()}
//            """.trimIndent()
//        )
//
//        val responseNode: JsonNode? = Objects.requireNonNull(restTemplate.postForObject(uri, requestNode, JsonNode::class.java))
//        log.info(
//            """
//                Authorization response:
//                ${responseNode?.toPrettyString()}
//            """.trimIndent()
//        )
//
//        return if (responseNode?.has("result") == true && responseNode.get("result")?.asBoolean() == true) {
//            AccessDecisionVoter.ACCESS_GRANTED
//        } else {
//            AccessDecisionVoter.ACCESS_DENIED
//        }
//    }
//}
