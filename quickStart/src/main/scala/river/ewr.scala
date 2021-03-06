package river
import java.net._
/**
  * @author tianfusheng
  * @e-mail linuxmorebetter@gmail.com
  * @date 2019/12/31
  */
object ewr {

  var i  = 0
  def main(args: Array[String]): Unit = {
    val city = "Nizhniy Novgorod"
    //    var res  = citiesOnRiver("Tolyatti")
    //    println(res)
    //var bc = Set[String]()
    //val res1 = getCities("Nizhniy Novgorod",bc)
    //println("ALL CITY:"+res1)
    var res = getCities(city)

    println(res)
  }


  //select t2.* from located t1 inner join located t2 on t1.River = t2.River where t1.City='Nizhniy Novgorod' and t2.City !='Nizhniy Novgorod'
  /*  def getCities(city: String,bc:Set[String]): Set[String] = {

      val sql ="select t2.city,t2.river from located t1 inner join located t2 on t1.River = t2.River where t1.City='"+city+"'"
      i=i+1
      println("------deepin: "+i)
      println(sql)
      val eq = URLEncoder.encode(sql, "UTF-8")
      val u = new java.net.URL(
        "http://kr.unige.ch/phpmyadmin/query.php?db=Mondial"+"&sql="+eq)
      val in = scala.io.Source.fromURL(u, "iso-8859-1")
      var res = Set[String]()
      var riverSet = Set[String]()
      for (line <- in.getLines) {
        val cols = line.split("\t")
        res += cols(0)
        riverSet += cols(1)
      }
      in.close()
      println("res:"+res)
      println("river on "+city+" is:"+riverSet)
      println("bc:"+bc)
      val re =res++bc
      val nextSet = res--bc
      println("nextSet:"+nextSet)

      if(nextSet.size!=0){
        for (c <- nextSet){
          getCities(c,re)
        }
      }
      re
    }*/



  //
  def getCities(city: String): Set[String] ={
    val q = "select river from located where city = '" + city + "'"
    val eq = URLEncoder.encode(q, "UTF-8")
    val u = new java.net.URL("http://kr.unige.ch/phpmyadmin/query.php?db=Mondial"+"&sql="+eq)
    val in = scala.io.Source.fromURL(u, "iso-8859-1")
    var river = Set[String]()
    for (line <- in.getLines) {
      val cols = line.split("\t")
      river = river ++ getAllRivers(cols(0),river)
    }
    in.close()

    var res = Set[String]()
    for (r <- river){
      res = res ++ citiesOnRiver(r)
    }
    res
  }

  def getAllRivers(river:String,res:Set[String] ): Set[String] ={
    val sql = "select River from river where Name ='"+river+"'"
    i+=1
    println("deepin:"+(i)+" , sql : "+sql)
    val eq = URLEncoder.encode(sql, "UTF-8")
    val u = new java.net.URL("http://kr.unige.ch/phpmyadmin/query.php?db=Mondial"+"&sql="+eq)
    val in = scala.io.Source.fromURL(u, "iso-8859-1")
    var bc = res
    for (line <- in.getLines) {
      val cols = line.split("\t")
      if(cols(0)!=null&&cols(0)!=""){
        bc = res  ++ getAllRivers(cols(0),bc)
      }
    }
    in.close()
    bc+=river
    bc
  }



  def citiesOnRiver(r: String): Set[String] = {
    val q = "select city from located where river = '" + r + "'"
    val eq = URLEncoder.encode(q, "UTF-8")
    val u = new java.net.URL(
      "http://kr.unige.ch/phpmyadmin/query.php?db=Mondial"+"&sql="+eq)
    val in = scala.io.Source.fromURL(u, "iso-8859-1")
    var res = Set[String]()
    for (line <- in.getLines) {
      val cols = line.split("\t")
      res += cols(0)
    }
    in.close()
    res
  }


}
