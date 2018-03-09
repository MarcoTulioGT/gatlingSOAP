/**
 * copyright 2011-2018 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package BPM

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class TestSOAP_data extends Simulation {
val feeder = csv("datatestSOAP.csv").queue
val httpProtocol = http
            .baseURL("http://bpm.tigo.com.gt:8001/soa-infra/services/default/CustomDataBPMService/")

    val header = Map(
    "accept-encoding" -> "gzip, deflate",
    "Content-Type" -> "text/xml;charset=UTF-8",
    "SOAPAction" -> "addProcessData", "Content-Length" -> "275",
    "Host" -> "bpm.tigo.com.gt:8001",
    "Connection" -> "alive",
    "accept-language" -> "en-US,en;q=0.8",
    "user-agent" -> "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
	
val scn = scenario("SOAPRecordedSimulation")
          .feed(feeder)
            .exec(http("BPM SOA addProcessData")
            .post("CustomDataBPMService_client_ep")
            .headers(header)
            .body(StringBody("""<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
<soap:Body>
<tns:processDataRequest xmlns:cdt="http://www.tigo.com.gt/bpm/common/data/types/v1" xmlns:tns="http://www.tigo.com.gt/soa/TEF
/CustomBPMData">
-<tns:element>
<tns:flowId>${flowId}</tns:flowId>
<tns:instanceId>${instanceId}</tns:instanceId>
<tns:name>${name}</tns:name>
<tns:type>${type}</tns:type>
<tns:value>${value}</tns:value>
</tns:element>
</tns:processDataRequest>
</soap:Body>
</soap:Envelope>""")))

        setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
}
