---
--- Created by Bk.
--- DateTime: 6/19/2025 8:11 PM
---
require('lava')

Vec2 = import('com.badlogic.gdx.math.Vector2')
---@alias Vec2 Vec2
---@type Vec2
Rect = import('com.badlogic.gdx.math.Rectangle')
---@alias Rect Rect
---@type Rect




---@param x number
---@param y number
---@return Vec2
function newVec2(x, y)
    return java.new(Vec2, x, y)
end


function newRect(x,y, width, height)
    return java.new(Rect,x,y,width,height)
end
