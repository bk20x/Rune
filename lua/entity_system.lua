---
--- Created by Bk.
--- DateTime: 6/19/2025 1:44 PM
---
require('entity')
require('lava')
EntitySystem = import('rune.editor.system.EntityManagerr')
entitySystem = java.new(EntitySystem)



Entities = {}
function Entities:new()
    local new = setmetatable({}, { __index = Entities })
    return new
end

---@param id integer
function Entities:get(id)
    return Entities[id]
end

---@param entity Entity
function AddLocalEntity(entity)
    entities[entity.id] = entity
end


---@param entity Entity
function AddEntity(entity)
    entitySystem:add(entity)
end


---@param entities Entity[]
function AddAll(entities)
    for i in ipairs(entities) do
        entitySystem:add(entities[i])
    end
end




