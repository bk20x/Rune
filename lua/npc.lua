---@diagnostic disable: undefined-doc-name, undefined-field
---
--- Created by Bk.
--- DateTime: 7/10/2025 3:30 AM
---
require('lava')
require('items')

__Npc = import('rune.editor.npc.Npc')
---@class Npc
---@field actor __Npc
Npc = {name = nil,
       actor = __Npc:Empty()}

function Npc:New(t)
    t = t or {}
    if t.name ~= nil then
        t.actor = __Npc:New(t.name)
    end
    setmetatable(t, self)
    self.__index = self
    return t
end

function Npc:respondTo(option)
    return self.actor:getDialogueResponseAndWalkTree(option)
end



ShopTypes = import('rune.editor.types.ShopTypes')
---@class Shop
---@field type ShopTypes
---@field trader Npc
---@field items {}
Shop = { type = nil,
         npc = nil,
         items = {},
         open = false}


function Shop:New(t)
    t = t or {}
    setmetatable(t, self)
    self.__index = self
    return t
end

function Shop:sellItem(player, itemname)
    for k, item in pairs(self.items) do
        if item.name == itemname and player.gold >= item.cost then
            player:addItem(item)
            player.gold = player.gold - item.cost
            item = nil
            return
        end
    end
end

function Shop:open()
    self.npc.actor:setCurrentBranch('trade')
    self.open = true
end

function Shop:close()
    self.npc.actor:setBranchToDefault()
    self.open = false
end

function Shop:addItem(item)
    table.insert(self.items,item)
end

function Shop:addItems(items)
    for k,item in pairs(items) do
        self:addItem(item)
    end
end





