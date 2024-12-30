import { z } from 'zod';
import { ResearcherList, ResearcherStats } from './researcherTypes';
import { User } from './userTypes';

export type Comparison = {
  id: number;
  name: string;
  list1: ResearcherList;
  list2: ResearcherList;
  createdBy: User;
};

export const ComparisonCreateRequestSchema = z.object({
  name: z.string().min(1, { message: 'error.fieldCantBeBlank' }),
});

export type ComparisonCreateRequest = z.infer<typeof ComparisonCreateRequestSchema> & {
  list1: number;
  list2: number;
};

export interface ComparisonDetail {
  id: number;
  name: string;
  list1Analysis: ComparisonAnalysis;
  list2Analysis: ComparisonAnalysis;
}

export interface ComparisonAnalysis {
  researcherListName: string;
  mean: number;
  researcherStats: ResearcherStats[];
}
