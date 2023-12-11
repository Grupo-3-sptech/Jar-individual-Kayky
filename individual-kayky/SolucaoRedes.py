import psutil
from datetime import datetime
import time
import mysql.connector
import ping3
import pymssql


sqlserver_connection = pymssql.connect(server='52.7.105.138', database='medconnect', user='sa', password='medconnect123');


url = "youtube.com"

idRobo = 1
#descomente abaixo quando for ora criar esse arquivo pelo kotlin
idRobo = 9

listaComponentes = [15,16,17,18,19]



while True:
        for i in range(len(listaComponentes)):
                try:
                  horarioAtual = datetime.now()
                  horarioFormatado = horarioAtual.strftime('%Y-%m-%d %H:%M:%S')

                  conexaoAtiva = psutil.net_if_stats()['Wi-Fi'].isup
                  latenciaRede = round(ping3.ping(url) * 1000, 2)
                  velocidadeRede = psutil.net_if_stats()['Wi-Fi'].speed
                  bytesEnviados = psutil.net_io_counters().bytes_sent
                  bytesRecebidos = psutil.net_io_counters().bytes_recv

                  listaDados = [conexaoAtiva, latenciaRede, bytesEnviados, bytesRecebidos, velocidadeRede]
        
                  query = '''INSERT INTO Registros (fkRoboRegistro, HorarioDado, dado, fkComponente)
                     VALUES
                     (%s, %s, %s, %s) 
                '''
                  server_cursor = sqlserver_connection.cursor()
          
                  server_cursor.execute(query, (idRobo, horarioFormatado, listaDados[i], listaComponentes[i]))

                  sqlserver_connection.commit()
                except Exception as e:
                        print(e)
          
        print("Informações sobre a Rede:")
        print()
        print("Estado da conexão", conexaoAtiva)
        print()
        print("Latência da Rede", latenciaRede)
        print()
        print("Velocidade da Rede", velocidadeRede)
        print()
        print("Bytes enviados", bytesEnviados)
        print()
        print("Bytes recebidos", bytesRecebidos)
        print()
        time.sleep(5)
server_cursor.close()
sqlserver_connection.close()
            
        
