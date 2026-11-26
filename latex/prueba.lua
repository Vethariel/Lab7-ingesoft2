-- \prueba{nombre_de_la_prueba}: permite cortar el nombre en cada guion bajo y antes de
-- cada mayúscula del camelCase, para que los nombres largos no se salgan del margen.
function prueba(nombre)
  local s = nombre:gsub("(%u)", "\\allowbreak %1"):gsub("_", "\\textunderscore\\allowbreak ")
  tex.sprint(s)
end
