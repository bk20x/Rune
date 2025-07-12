---
--- Created by Bk.
--- DateTime: 6/20/2025 6:51 AM
---
require('lava')
require('sub/gdx_math')

Input = import('com.badlogic.gdx.Gdx').input
Keys = import('com.badlogic.gdx.Input.Keys')
Game = import('rune.editor.State')
Renderer = import('rune.editor.Renderer')

OrthoCam = import('com.badlogic.gdx.graphics.OrthographicCamera')
---@alias OrthoCam OrthoCam
---@type OrthoCam

---@param width integer
---@param height integer
---@return OrthoCam
function newCamera(width,height)
    return java.new(OrthoCam,width,height)
end

---@param target Vec2
function follow(cam,target)
    cam.pos:set(target.x,target.x,0)
end

keyDown = java.method(Input, 'isKeyPressed','int')
keyDownOnce = java.method(Input, 'isKeyJustPressed', 'int')

