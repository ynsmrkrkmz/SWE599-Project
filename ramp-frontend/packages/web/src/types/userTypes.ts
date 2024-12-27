import refinePasswordStrength from '@ramp/utils/refinePasswordStrength';
import { z } from 'zod';

export type User = {
  id: number;
  name: string;
  lastname: string;
  email: string;
};

export const LoginFormInputSchema = z.object({
  email: z
    .string()
    .min(1, { message: 'error.emailCantBeBlank' })
    .email({ message: 'error.invalidEmail' }),
  password: z.string().min(1, { message: 'error.passwordCantBeBlank' }),
});

export type LoginFormInput = z.infer<typeof LoginFormInputSchema>;

export const SignupFormInputSchema = z
  .object({
    name: z.string().min(1, { message: 'error.nameCantBeBlank' }),
    lastname: z.string().min(1, { message: 'error.lastnameCantBeBlank' }),
    email: z
      .string()
      .min(1, { message: 'error.emailCantBeBlank' })
      .email({ message: 'error.invalidEmail' }),
    password: z.string().min(1, { message: 'error.passwordCantBeBlank' }),
    confirm: z.string(),
    role: z.number().optional(),
  })
  .refine((data: { password: string; confirm: string }) => data.password === data.confirm, {
    message: 'error.passwordsNotMatch',
    path: ['confirm'], // path of error
  })
  .refine(...refinePasswordStrength);

export type SignupFormInput = z.infer<typeof SignupFormInputSchema>;
