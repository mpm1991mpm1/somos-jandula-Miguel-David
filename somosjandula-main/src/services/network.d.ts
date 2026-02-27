/*************************************************/
/*************** Tipos de Redes *******************/
/*************************************************/

declare module '@/services/network' {
  export type SeguridadRed = 'WPA2' | 'WEP' | 'OPEN' | (string & {});

  /** Registro devuelto por /estado-actual */
  export interface EstadoRedRegistro {
    id?: number;
    ssid: string;
    estado: string;
    fechaReporte?: string;
  }

  /** Elemento devuelto por /configuracion-redes */
  export interface RedConfigurada {
    id: number;
    ssid: string;
    seguridad: SeguridadRed;
  }

  /** Payload para registrar una red a monitorizar */
  export interface RedNueva {
    nombre: string;
    usuario: string;
    contrasena: string;
    seguridad: SeguridadRed;
  }

  /** Configuración persistida del intervalo de monitorización */
  export interface MonitorizacionConfig {
    refreshMinutes: number | null;
  }

  /*************************************************/
  /****************** Servicios *********************/
  /*************************************************/

  export function obtenerEstadoActual(): Promise<EstadoRedRegistro[]>;

  export function obtenerRedesConfiguradas(): Promise<RedConfigurada[]>;

  export function registrarRed(red: RedNueva): Promise<string>;

  export function eliminarRed(id: number | string): Promise<string>;

  export function obtenerConfiguracionMonitorizacion(): Promise<MonitorizacionConfig>;

  export function guardarConfiguracionMonitorizacion(refreshMinutes: number): Promise<MonitorizacionConfig>;
}
