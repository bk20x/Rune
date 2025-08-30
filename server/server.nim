import std/[net,os,strutils,json]
import more_sugar


type 
  Player = object
    name: string
    posX, posY, health: float

  User = tuple[player: Player, ip: IpAddress, port: Port]

  GameState = ref object
    activeScene: string

var
   users: seq[User]
   state: GameState
   started: bool = false


proc acceptJoin(packet: sink string, ip: sink IpAddress, port: sink Port): -> void =
  if users.len < 2:
    if users.len > 0:
      each users, user:
        if user.player.name != packet[5..^1]:
          player <- parseJson(packet[5..^1]).to Player
          users.add (player, ip, port)
          echo "Player joined: ", player
    else:
      player <- parseJson(packet[5..^1]).to Player
      users.add (player, ip, port)
      echo "Player joined: ", player
      



proc updatePlayers(packet: sink string, server: Socket): -> void =
  try:
    player <- parseJson(packet).to Player
    each users.mitems, user:
      if user.player.name == player.name:
        user.player = player
      else:
        server.sendTo user.ip, user.port, $(%player)
  except Exception:
    echo "Error updating, recvd: ", packet


proc processPacket(packet: string, ip: IpAddress, port: Port, server: Socket): -> void =
  if "join!" in packet:
    acceptJoin packet, ip, port
  else:
    updatePlayers packet, server


proc main(): -> void = 
  var server = newSocket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
  defer: close server
  server.bindAddr Port 8080
  echo "Listening on Port: ", 8080

  while true:
    var
      data: string
      sender: IpAddress 
      port: Port
    try:
      if server.recvFrom(data, 1024, sender, port) != 0:
        processPacket data, sender, port, server
    except IOError as e:
      echo "An IO error occurred: ", e.msg

    except Exception as e:
      echo "Something went wrong:  ", e.msg

main()