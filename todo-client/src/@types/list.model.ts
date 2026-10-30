interface BaseList {
  name: string;
  description: string;
}

export interface ListRequest extends BaseList {}

export interface ListEdit extends BaseList {
  id: number;
}

export interface ListResponse extends BaseList {
  id: number;
  totalTasks: number;
  completedTasks: number;
  completionPercentage: number;
}
