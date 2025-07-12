---
--- Created by Bk.
--- DateTime: 6/17/2025 10:30 PM
---
require('lava')

Entity = import('rune.editor.entity.Entity')
---@alias Entity Entity
---@type Entity

ETypes = import('rune.editor.types.EntityTypes') ----STATIC_OBJ
---@alias ETypes ETypes                          ----ANIMATED_OBJ
---@type ETypes                                  ----MOB

Mobs = import('rune.editor.objects.Mobs')
---@alias Mobs Mobs
---@type Mobs


---@param type ETypes
---@return Entity
function NewEntity(name,type)
    local res = java.new(Entity, name, type)
    return res
end


---@param entities Entity[]
function BatchUpdate(entities)
    for i in ipairs(entities) do
        entities[i]:update()
    end
end

---@param entity Entity
function Draw(entity, x, y)
    entity:draw(x,y)
end


---@param mob Entity
function onPlayerContact(mob,player)
    if mob.bounds:overlaps(player.bounds) then
        local hit = function()

      end
   end
end
