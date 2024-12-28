import { z } from 'zod';
import { User } from './userTypes';

export type Researcher = {
  id: number;
  openAlexId: string;
  orcId: string;
  name: string;
  institution: string;
  institutionCountry: string;
};

export type ResearcherList = {
  id: number;
  name: string;
  createdBy: User;
};

export const ResearcherListCreateRequestSchema = z.object({
  name: z.string().min(1, { message: 'error.fieldCantBeBlank' }),
});

export type ResearcherListCreateRequest = z.infer<typeof ResearcherListCreateRequestSchema>;

export type ResearcherListDetail = ResearcherList & {
  researchers: Researcher[];
};

export const ResearcherSearchSchema = z.object({
  researcherName: z.string().min(1, { message: 'error.fieldCantBeBlank' }),
});

export type ResearcherSearch = z.infer<typeof ResearcherSearchSchema>;

export type ResearcherListMembershipCreateRequest = {
  researcherListId: number;
  openAlexId: string;
  orcId: string;
  name: string;
  institution: string;
  institutionCountry: string;
};
