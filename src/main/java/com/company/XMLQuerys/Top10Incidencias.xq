let $start:=1
let $records:=10

let $sorted-incidencias :=
    for $incidencia in /IncidenciasReportadas/Incidencia
    order by ($incidencia/horas/fn:number()) descending
    return $incidencia

for $incidencia in fn:subsequence($sorted-incidencias, $start, $records)
return $incidencia