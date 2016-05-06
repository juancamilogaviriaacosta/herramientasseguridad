/*
HERRAMIENTAS DE SEGURIDAD
JUAN CAMILO GAVIRIA
NICOLAS PINZON
*/


/* Definicion de usurios */
usuario(juan).
usuario(marcela).
usuario(adriana).
usuario(admin).

/* Definicion de recursos */
recurso(musica).
recurso(tarea).
recurso(fotos).

/* Definicion de permisos */
permiso(leer).
permiso(escribir).

/* Asignaciones	de permisos a usuarios sobre recursos */
:- dynamic usuario_recurso/3.
usuario_recurso(juan,musica,leer).
usuario_recurso(juan,musica,escribir).
usuario_recurso(marcela,musica,leer).

/* Definicion de grupos */
grupo(administradores).
grupo(usuarios).

/* Asignacion de permisos a grupos */
grupo_permiso(administradores, musica, leer).
grupo_permiso(administradores, musica, escribir).


/* Asignacion de usuarios a grupos */
grupo_usuario(administradores, admin).

/* Agrega dinamicamente un predicado usuario_recuros(U,R,P) que que se obtiene de las definiciones de grupos y que no este repetido */
agregar_predicados:- 
  grupo_usuario(G,U), grupo_permiso(G,R,P), 
  usuario(U), recurso(R), permiso(P), 
  (not(usuario_recurso(U,R,P)) -> assert(usuario_recurso(U,R,P))).

/* Definicion de reglas de inferencia */
existe_flujo_info(A,B):-
  findall(_, agregar_predicados, _),
  usuario(A), /* A es un usuario */
  usuario(B), /* B es un usuario */
  (
    findall(RA, (usuario_recurso(A,RA,PA), recurso(RA), permiso(PA), usuario_recurso(B,RA,PB), A\=B, PA\=PB),LRA), /* Encuentra todos los recursos y permisos de A de tal modo que exista un usuario B con ese recurso pero permiso contrario */
    findall(RB, (usuario_recurso(B,RB,PB), recurso(RB), permiso(PB), usuario_recurso(A,RB,PA), B\=A, PA\=PB),LRB), /* Encuentra todos los recursos y permisos de B de tal modo que exista un usuario A con ese recurso pero permiso contrario */
    (interseccion(LRA, LRB, L),!) /* Encuentra la interseccion entre los recursos de A y los de B */
  ),
  L\=[],!. /* Si la interesccion NO es vacia, la respuesta es verdadera */




/* Todas las parejas de usuarios que pueden intercambiar informacion */
todos(L2):- 
findall(_, agregar_predicados, _),
findall((U,B), (usuario_recurso(U,RU,PU), recurso(RU), permiso(PU), usuario_recurso(B,RU,PB), U\=B, PU\=PB),L1),
eliminar_repetios(L1, L2).



/*----------------------------------------Utilidades----------------------------------------*/

interseccion([], _, []).

interseccion([H1|T1], L2, [H1|Res]) :-
    member(H1, L2),
    interseccion(T1, L2, Res).

interseccion([_|T1], L2, Res) :-
    interseccion(T1, L2, Res).

eliminar_repetios([], []).
eliminar_repetios([H|T], [H|T1]) :- subtract(T, [H], T2), eliminar_repetios(T2, T1).


/*--------------------------------------Casos de prueba-------------------------------------*/
/*
existe_flujo_info(juan, marcela).
existe_flujo_info(juan, adriana).
existe_flujo_info(juan, usuario_falso).
todos(L).
*/
