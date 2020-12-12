package ch.keepcalm.demo

import ch.keepcalm.bar.BarService
import ch.keepcalm.foo.FooService
import ch.keepcalm.hello.HelloService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.boot.ApplicationRunner

@SpringBootApplication
class OPADemoApplication

fun main(args: Array<String>) {
	runApplication<OPADemoApplication>(*args) {
		addInitializers(
			beans {
				bean {
					ApplicationRunner {

						ref<FooService>().sayFoo()
						ref<HelloService>().sayHello()

						println(":::: ApplicationRunner :::: ")

						ref<BarService>().sayBye()
					}
				}
			}
		)
	}
}

