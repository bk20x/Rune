---
--- Created by Bk.
--- DateTime: 7/14/2025 7:08 AM
---
require('lava')
Direction = import('rune.editor.types.DIRECTION')
Rarity = import('rune.editor.types.Rarity')
Input = import('com.badlogic.gdx.Gdx').input
Keys = import('com.badlogic.gdx.Input.Keys')
Game = import('rune.editor.Game')
Renderer = import('rune.editor.Renderer')

function GetDeltaTime()
    return Gdx.graphics:getDeltaTime()
end

require('sub/gdx_math')


OrthoCam = import('com.badlogic.gdx.graphics.OrthographicCamera')
---@alias OrthoCam OrthoCam
---@type OrthoCam

---@param width integer
---@param height integer
---@return OrthoCam
function Camera(width,height)
    return java.new(OrthoCam,width,height)
end

---@param target Vec2
function Follow(cam,x,y)
    cam.pos:set(target.x,target.x,0)
end

keyDown = java.method(Input, 'isKeyPressed','int')
keyDownOnce = java.method(Input, 'isKeyJustPressed', 'int')

