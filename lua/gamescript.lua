
---
--- Created by Bk.
--- DateTime: 6/20/2025 7:19 AM
---
require('entity')
require('sceneutils')
require('math')
require('engine_globals')
require('items')
require('game')
require('npc')
Player = import('rune.editor.Player')



ShapeRenderer = import('rune.editor.ShapeRenderer')
Gdx = import('com.badlogic.gdx.Gdx')

local game = import('rune.editor.GameScreen').runeGame


local camera = Camera(640,360)
local player = Player:New()
local scene = Scene:New()






game:setScene(scene) ---setScene sets the active scene in the game, for ex. entering a dungeon switches the scene
game:addPlayer(player) ---important for the player to interact with the scene && the players draw method is called with game:render()




local viewBounds = newRect(0,0,0,0)







local function zoom()
    if keyDown(Keys.UP) and camera.zoom > 0.1 then
        camera.zoom = camera.zoom - 0.01
    else
        if keyDown(Keys.DOWN) then
            camera.zoom = camera.zoom + 0.01
        end
    end
end


local function updateCamera()
    camera.position.y = player.pos.y + 16
    camera.position.x = player.pos.x + 16

    camera:update()
    game:setView(camera)
end



local dungeon = Scenes:Dungeon("DungeonMap1")
game:setScene(dungeon)

local n = Item:New { name = "health potion" }

System.out:println(n.ref.effect)
function main()
    updateCamera()
    if keyDown(Keys.T) then
        System.out:println(player.inventory:size())
        for key, ent in pairs(RandomEnts()) do
            game:addEntity(ent)
        end
    end


    if keyDown(Keys.L) then

        local newScene = Scene:New()

        player.pos.x = 70
        player.direction = Direction.WEST
        player.isMoving = true
    end


    zoom()
end
