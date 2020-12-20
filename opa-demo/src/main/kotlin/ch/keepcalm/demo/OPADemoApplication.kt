package ch.keepcalm.demo

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
						println(":::: ApplicationRunner :::: ")
					}
				}
			}
		)
	}
}

