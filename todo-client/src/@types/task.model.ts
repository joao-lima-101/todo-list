interface TaskBase {
  name: string;
  description: string;
  dueDate: string | null;
  done: boolean;
  priority: 'ALTO' | 'MEDIO' | 'BAIXO';
}

export interface TaskRequest extends TaskBase {}

export interface TaskEdit extends TaskBase {
  idList: number;
  id: number;
}

export interface TaskResponse extends TaskBase {
  id: number;
}
