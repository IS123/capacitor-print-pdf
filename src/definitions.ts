export interface PrintPdfPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
