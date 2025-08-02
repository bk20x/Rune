
---
--- Created by Bk.
--- DateTime: 6/20/2025 7:19 AM
---
require('entity')
require('sceneutils')
require('math')
require('engine_globals')
require('items')
require('npc')



ShapeRenderer = import('rune.editor.ShapeRenderer')
Gdx = import('com.badlogic.gdx.Gdx')
local Player = import('rune.editor.Player')
local game = import('rune.editor.GameScreen').gameState


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



if game.camera == nil then
    camera = Camera(640,360)
end


if game.scene == nil then
    local dungeon = Scenes:Dungeon("DungeonMap1")
    game:setScene(dungeon)
end

if game.player == nil then
    player = Player:New()
    game:addPlayer(player)
end

local n = Item:New { name = "health potion" }


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
