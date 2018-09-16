package scala.other.jsondm

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
case class SdkBean(
                          appkey: String,
                          timestamp: String,
                          cookie: String,
                          short_cookie: String,
                          request_method: String,

                          status: java.lang.Integer,
                          http_referer: String,
                          http_user_agent: String,
                          http_x_forwarded_for: String,
                          http_url: String,

                          to_target: String,
                          duration: java.lang.Integer,
                          event: String,
                          is_new: java.lang.Integer,
                          page_id: String,
                          day: String
                  )
