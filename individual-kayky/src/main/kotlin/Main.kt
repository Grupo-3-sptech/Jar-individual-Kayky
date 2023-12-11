package app

import com.github.britooo.looca.api.core.Looca
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.JdbcTemplate
import java.io.File
import java.util.Scanner



class Login {
    var conexaoBancoDeDados: JdbcTemplate
    var id = Looca().processador.id
    var scan = Scanner(System.`in`)


    init {

        val dataSoruceServer = BasicDataSource()
        dataSoruceServer.url = "jdbc:sqlserver://52.7.105.138:1433;databaseName=medconnect;encrypt=false";
        dataSoruceServer.driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        dataSoruceServer.username = "sa"
        dataSoruceServer.password = "medconnect123"
        conexaoBancoDeDados = JdbcTemplate(dataSoruceServer)
    }

    fun verificacao() {


        print(id)
        val qtdIds = conexaoBancoDeDados.queryForObject(
            """
    SELECT COUNT(*) AS count FROM RoboCirurgiao WHERE idProcess = '$id'
    """,
            Int::class.java,
        )

        if (qtdIds == 0) {
            loguinMaquina()
        } else {
            fim()
        }

    }
    

    fun fim() {
        println("Coletando dados")

        val idRobo = conexaoBancoDeDados.queryForObject(
            "SELECT idRobo FROM RoboCirurgiao WHERE idProcess = ?",
            Int::class.java,
            id
        ) ?: 0

        while (true) {


            val roboId = conexaoBancoDeDados.queryForObject(
                """
    select idRobo from RoboCirurgiao where idProcess = '$id'
    """,
                Int::class.java,
            )


            val nomePython = "SolucaoRedes.py"
            val arquivoPython = File(nomePython)
            arquivoPython.writeText("import psutil\n" +
                    "from datetime import datetime\n" +
                    "import time\n" +
                    "import mysql.connector\n" +
                    "import ping3\n" +
                    "import pymssql\n" +
                    "\n" +
                    "\n" +
                    "sqlserver_connection = pymssql.connect(server='52.7.105.138', database='medconnect', user='sa', password='medconnect123');\n" +
                    "\n" +
                    "\n" +
                    "url = \"youtube.com\"\n" +
                    "\n" +
                    "idRobo = 1\n" +
                    "#descomente abaixo quando for ora criar esse arquivo pelo kotlin\n" +
                    "idRobo = ${roboId}\n" +
                    "\n" +
                    "listaComponentes = [15,16,17,18,19]\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "while True:\n" +
                    "        for i in range(len(listaComponentes)):\n" +
                    "                try:\n" +
                    "                  horarioAtual = datetime.now()\n" +
                    "                  horarioFormatado = horarioAtual.strftime('%Y-%m-%d %H:%M:%S')\n" +
                    "\n" +
                    "                  conexaoAtiva = psutil.net_if_stats()['Wi-Fi'].isup\n" +
                    "                  latenciaRede = round(ping3.ping(url) * 1000, 2)\n" +
                    "                  velocidadeRede = psutil.net_if_stats()['Wi-Fi'].speed\n" +
                    "                  bytesEnviados = psutil.net_io_counters().bytes_sent\n" +
                    "                  bytesRecebidos = psutil.net_io_counters().bytes_recv\n" +
                    "\n" +
                    "                  listaDados = [conexaoAtiva, latenciaRede, bytesEnviados, bytesRecebidos, velocidadeRede]\n" +
                    "        \n" +
                    "                  query = '''INSERT INTO Registros (fkRoboRegistro, HorarioDado, dado, fkComponente)\n" +
                    "                     VALUES\n" +
                    "                     (%s, %s, %s, %s) \n" +
                    "                '''\n" +
                    "                  server_cursor = sqlserver_connection.cursor()\n" +
                    "          \n" +
                    "                  server_cursor.execute(query, (idRobo, horarioFormatado, listaDados[i], listaComponentes[i]))\n" +
                    "\n" +
                    "                  sqlserver_connection.commit()\n" +
                    "                except Exception as e:\n" +
                    "                        print(e)\n" +
                    "          \n" +
                    "        print(\"Informações sobre a Rede:\")\n" +
                    "        print()\n" +
                    "        print(\"Estado da conexão\", conexaoAtiva)\n" +
                    "        print()\n" +
                    "        print(\"Latência da Rede\", latenciaRede)\n" +
                    "        print()\n" +
                    "        print(\"Velocidade da Rede\", velocidadeRede)\n" +
                    "        print()\n" +
                    "        print(\"Bytes enviados\", bytesEnviados)\n" +
                    "        print()\n" +
                    "        print(\"Bytes recebidos\", bytesRecebidos)\n" +
                    "        print()\n" +
                    "        time.sleep(5)\n" +
                    "server_cursor.close()\n" +
                    "sqlserver_connection.close()\n" +
                    "            \n" +
                    "        \n"
            )}

            Thread.sleep(200 * 20000)
        

    }


    fun installPyhon() {



    }

    fun cadastroMaquina(fkHospital: Int) {

        conexaoBancoDeDados.execute(
            """
                INSERT INTO RoboCirurgiao (modelo, fabricacao, fkStatus, idProcess, fkHospital) 
VALUES ('Modelo A', '${Looca().processador.fabricante}', 1, '$id', $fkHospital);
                
                """
        )
        println("parabéns robo cadastrado baixando agora a solução MEDCONNECT")

        installPyhon()
        fim()

    }


    fun loguinMaquina() {
        var pode = false
        println("para verificar se tem as credenciais necessárias digite seu email")
        var email = scan.nextLine()
        println("")
        println("agora sua senha")
        var senha = scan.nextLine()
        println("")

        var fkHospital = conexaoBancoDeDados.queryForObject(
            """
    select fkHospital from usuario
    where email = '$email' AND senha = '$senha'
    """,
            Int::class.java
        )


        if (fkHospital != null) {
            pode = true
        }


        if (pode) {
            print("começando a registrar a maquina em nosso banco de dados")
            cadastroMaquina(fkHospital)

        } else {
            println("problema de autenticação")
        }


    }
}





open class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val User = Login()
            User.verificacao()
        }
    }
}