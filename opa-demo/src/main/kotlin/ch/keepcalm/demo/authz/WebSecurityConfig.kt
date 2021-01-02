//package ch.keepcalm.demo.authz
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.annotation.Order
//import org.springframework.security.access.AccessDecisionManager
//import org.springframework.security.access.AccessDecisionVoter
//import org.springframework.security.access.vote.UnanimousBased
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//
//@Configuration
//@EnableWebSecurity
//@Order(101)
//class WebSecurityConfig : WebSecurityConfigurerAdapter() {
//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        http.authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager())
//    }
//
//    @Bean
//    fun accessDecisionManager(): AccessDecisionManager {
//        val decisionVoters: List<AccessDecisionVoter<out Any?>> = listOf(OPAVoter("http://localhost:8181/v1/data/http/authz/allow"))
//        return UnanimousBased(decisionVoters)
//    }
//}
