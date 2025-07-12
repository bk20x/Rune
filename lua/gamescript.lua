---
--- Created by Bk.
--- DateTime: 6/20/2025 7:19 AM
---
require('entity')
require('sceneutils')
require('game')
require('math')
require('mobscript')

Player = import('rune.editor.Player')
World = import('rune.editor.scene.World')

ShapeRenderer = import('rune.editor.ShapeRenderer')
Gdx = import('com.badlogic.gdx.Gdx')

local sr = ShapeRenderer:New()

local camera = newCamera(640,360)
local player = Player:New()
local scene = Scene:New()
local world = World:New()

-- local dungeon = Scenes:DungeonMap("dungeon1")


world:setScene(scene) ---setScene sets the active scene in the world, for ex. entering a dungeon switches the scene

world:addPlayer(player) ---important for the player to interact with the scene && the players draw method is called with world:render()




local viewBounds = newRect(0,0,0,0)


local r = Renderer:New()


local function random()
    local ents = { Mobs:BlueSlime(), Mobs:GreenSlime(), Mobs:BlueSlime(),Mobs:OrangeSlime(), Mobs:PurpleSlime(), Mobs:PurpleSlime() }
    for key, entity in pairs(ents) do
        entity:setDirection(Direction:Random())
        entity:setPos(math.random(0,1280 - 32),math.random(0,1280 - 32))
    end
    return ents
end


local function zoom()
    if keyDown(Keys.UP) and camera.zoom > 0.1 then
        camera.zoom = camera.zoom - 0.01
    else
        if keyDown(Keys.DOWN) then
            camera.zoom = camera.zoom + 0.01
        end
    end
end

local function update()
    if world.scene.entitySystem:size() < 10 then
          for i = 1, 8, 1 do
             for k, e in pairs(random())do
                 world:addEntity(e)
            end
        end
    end
    camera.position.y = player.pos.y + 16
    camera.position.x = player.pos.x + 16

    viewBounds.x = camera.position.x / player.pos.x
    viewBounds.y = camera.position.y / player.pos.y

    viewBounds.height = camera.viewportHeight
    viewBounds.width = camera.viewportWidth


    camera:update()
    world:setView(camera)
end


function draw()
local dt = Gdx.graphics:getDeltaTime()
    sr:start()

     update()
     world:render(r, dt)

     sr:rect(viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height)
     sr:stop()
    zoom()
end

