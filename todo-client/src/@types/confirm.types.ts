export interface ConfirmDialogData<T extends unknown[] = [], R = void> {
  onConfirm: (...args: T) => R;
  label?: string;
  title?: string;
}
