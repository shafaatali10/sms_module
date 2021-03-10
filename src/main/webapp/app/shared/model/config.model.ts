export interface IConfig {
  id?: number;
  configName?: string;
  configValue?: string;
  description?: string;
}

export const defaultValue: Readonly<IConfig> = {};
