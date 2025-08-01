---
--- Created by Bk.
--- DateTime: 6/20/2025 9:35 AM
---
require('lava')

__Item = import('rune.editor.objects.Item')
---@alias __Item __Item

Items = import('rune.editor.objects.Items')

---@class Item
---@field name string
---@field ref __Item
Item = {name = "",
        ref = Items:Empty()}

function Item:New(t)
    t = t or {}
    t.ref = Items:New(t.name)
    setmetatable(t, self)
    self.__index = self
    return t
end



