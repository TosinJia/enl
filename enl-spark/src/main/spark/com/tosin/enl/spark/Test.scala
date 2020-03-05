package com.tosin.enl.spark

import com.tosin.enl.common.time.TimeTranstationUtils

object Test {
  def main(args: Array[String]): Unit = {
    val info = TimeTranstationUtils.Date2yyyyMMddHHmmss(java.lang.Long.valueOf("1234569000"+"000"))
    println(info)
  }
}
