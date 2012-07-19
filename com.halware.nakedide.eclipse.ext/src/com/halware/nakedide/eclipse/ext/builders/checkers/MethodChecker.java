package com.halware.nakedide.eclipse.ext.builders.checkers;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.halware.nakedide.eclipse.core.Constants;
import com.halware.nakedide.eclipse.core.util.MethodUtils;
import com.halware.nakedide.eclipse.core.util.TypeUtils;

public class MethodChecker extends AbstractChecker {

	public MethodChecker(IMethod method) {
		this.method = method;
		this.type = method.getDeclaringType();
		this.methodName = method.getElementName();
		this.prefix = MethodUtils.getPrefix(methodName);
	}
	
	public final IType type;
	private final IMethod method;
	private final String methodName;
	private final String prefix;
	
	private String memberName;
	private String propertyOrCollectionAccessorMethodName;
	private String actionMethodName;
	private IMethod propertyOrCollectionAccessorMethod;
	private IMethod actionMethod;
	private boolean foundPropertyOrCollection;
	private boolean foundAction;
	private boolean isPropertyType;
	private boolean isCollectionType;
	private String returnTypeSig;
	private String[] paramTypeSigs;
	private String referencedTypeSig;
	private String[] referencedParamTypeSigs;

	public void checkPrefixes() throws CoreException {
		if (prefix == null) {
			return;
		}
        if (type.isInterface()) {
            return;
        }
        int flags = method.getFlags();
        if ((flags & Flags.AccPublic) != Flags.AccPublic) {
            return;
        }
		
		memberName = MethodUtils.unprefixed(method, prefix);
		propertyOrCollectionAccessorMethodName = MethodUtils.accessorMethodNameFor(memberName);
		actionMethodName = MethodUtils.actionMethodNameFor(memberName);
	
        propertyOrCollectionAccessorMethod = 
            TypeUtils.findMethodSearchingSuperclasses(type, propertyOrCollectionAccessorMethodName, new String[]{});
        foundPropertyOrCollection = propertyOrCollectionAccessorMethod != null;
        
        if (foundPropertyOrCollection) {
            foundAction = false;
        } else {
            actionMethod = TypeUtils.findMethodSearchingSuperclasses(type, actionMethodName, method.getParameterTypes());
            if (actionMethod == null) {
                actionMethod = TypeUtils.findMethodSearchingSuperclasses(type, actionMethodName);
            }
            foundAction = actionMethod != null;
        }
        
        // if no joy, we might be trying to match one of the supporting action methods
        // (disable, hide, default, choices) with a no-arg.  If so, attempt a more liberal search for
        // an action based only on the action name.
        if (!foundPropertyOrCollection && !foundAction) {
            if(method.getParameterTypes().length == 0 && 
               MethodUtils.in(prefix, Constants.ACTION_NO_ARG_SUPPORTING_PREFIXES)) {
                IMethod[] methods = type.getMethods();
                for(IMethod method: methods) {
                    if (method.getElementName().equals(actionMethodName)) {
                        actionMethod = method;
                        foundAction = true;
                    }
                }
            }
        }
		
		isPropertyType = true;
		isCollectionType = false;
		
		returnTypeSig = null;
		paramTypeSigs = null;
		referencedTypeSig = null;
		referencedParamTypeSigs = null;
		
		if (foundPropertyOrCollection) {
			
			returnTypeSig = propertyOrCollectionAccessorMethod.getReturnType();
			paramTypeSigs = new String[] { returnTypeSig };
			
			isCollectionType = MethodUtils.isCollectionTypeQSig(returnTypeSig);
			isPropertyType = !isCollectionType;
			
			if (isCollectionType) {
				referencedTypeSig = MethodUtils.collectionTypeQSig(returnTypeSig);
				referencedParamTypeSigs = new String[] { referencedTypeSig };
			}
		}
		
		if (foundAction) {
			paramTypeSigs = actionMethod.getParameterTypes(); 
		}

		if (foundPropertyOrCollection && foundAction) {
			createProblemMarker(
				method,
                MarkerConstants.ID_AMBIGUOUS_METHOD,
				"Ambiguous method (could represent both a " +
				propertyOrCollection(isPropertyType) +
				" and an action)");
			return;
		}

		
		if (prefix.equals(Constants.PREFIX_GET)) {
			// no additional checks
		} else if (prefix.equals(Constants.PREFIX_SET)) {
			checkSetPrefix();
		} else if (prefix.equals(Constants.PREFIX_MODIFY)) {
			checkModifyPrefix();
		} else if (prefix.equals(Constants.PREFIX_CLEAR)) {
			checkClearPrefix();
		} else if (prefix.equals(Constants.PREFIX_ADD_TO)) {
			checkAddToPrefix();
		} else if (prefix.equals(Constants.PREFIX_REMOVE_FROM)) {
			checkRemoveFromPrefix();
		} else if (prefix.equals(Constants.PREFIX_VALIDATE)) {
			checkValidatePrefix();
		} else if (prefix.equals(Constants.PREFIX_VALIDATE_ADD_TO)) {
			checkValidateAddToPrefix();
		} else if (prefix.equals(Constants.PREFIX_VALIDATE_REMOVE_FROM)) {
			checkValidateRemoveFromPrefix();
		} else if (prefix.equals(Constants.PREFIX_DISABLE)) {
			checkDisablePrefix();
		} else if (prefix.equals(Constants.PREFIX_HIDE)) {
			checkHidePrefix();
		} else if (prefix.equals(Constants.PREFIX_DEFAULT)) {
			checkDefaultPrefix();
		} else if (prefix.equals(Constants.PREFIX_CHOICES)) {
			checkChoicesPrefix();
		}
	}

    /**
	 * Applies to properties and collections only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 */
	private void checkSetPrefix() throws JavaModelException, CoreException {
		
        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_SET_METHOD,
                    "'set' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String mutatorMethodName = 
				MethodUtils.mutatorMethodNameFor(memberName);
			IMethod mutatorMethod = type.getMethod(mutatorMethodName, paramTypeSigs);
			
			if(MethodUtils.checkExists(mutatorMethod) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_MUTATOR_METHOD,
					"Mutator method parameter type for " +
					propertyOrCollection(isPropertyType) +
					" is incorrect (differs from accessor method return type)");
			}

		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_MUTATOR_METHOD,
				"Orphaned mutator method for " +
				propertyOrCollection(isPropertyType) +
				" (no corresponding accessor method)");
		}
	}
	
	/**
	 * applies to properties only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkModifyPrefix() throws JavaModelException, CoreException {
        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_MODIFY_METHOD,
                    "'modify' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String modifyMethodName = 
				MethodUtils.modifyMethodNameFor(memberName);
			IMethod modifyMethod = type.getMethod(modifyMethodName, paramTypeSigs);
			if(MethodUtils.checkExists(modifyMethod) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_MODIFY_METHOD,
					"'modify' method parameter type for property is incorrect (differs from accessor method return type)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_MODIFY_METHOD,
				"Orphaned 'modify' method for property (no corresponding accessor method)");
		}
	}

	/**
	 * applies to properties only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkClearPrefix() throws JavaModelException, CoreException {

		if (foundPropertyOrCollection) {
            if (method.getParameterNames().length > 0) {
                createProblemMarker(
                    method, 
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_CLEAR_METHOD,
                    "'clear' method for " + 
                    propertyOrCollection(isPropertyType) + 
                    " should have zero parameters.");
            }
		} else {
			createProblemMarker(
				method, 
                MarkerConstants.ID_ORPHANED_CLEAR_METHOD,
				"Orphaned 'clear' method for property (no corresponding accessor method)");
		}
	}

	/**
	 * applies to collections only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkAddToPrefix() throws JavaModelException, CoreException {

        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_ADD_TO_METHOD,
                    "'addTo' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String addToMethodName = 
				MethodUtils.addToMethodNameFor(memberName);
            if (referencedParamTypeSigs == null) {
                // cannot check any further since collection is not generic.
                return;
            }
			IMethod addToMethod = type.getMethod(addToMethodName, referencedParamTypeSigs);
			if(MethodUtils.checkExists(addToMethod) == null) {
				createProblemMarker(
					method,
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_ADD_TO_METHOD,
					"'addTo' method parameter type for collection is incorrect (should be parameterized type of the accessor method's return type)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_ADD_TO_METHOD,
				"Orphaned 'addTo' method for collection (no corresponding accessor method)");
		}
	}

	/**
	 * applies to collections only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkRemoveFromPrefix() throws JavaModelException, CoreException {

        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_REMOVE_FROM_METHOD,
                    "'removeFrom' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String removeFromMethodName = 
				MethodUtils.removeFromMethodNameFor(memberName);
            if (referencedParamTypeSigs == null) {
                // cannot check any further since collection is not generic.
                return;
            }
			IMethod removeFromMethod = type.getMethod(removeFromMethodName, referencedParamTypeSigs);
			if(MethodUtils.checkExists(removeFromMethod) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_REMOVE_FROM_METHOD,
					"'removeFrom' method parameter type for collection is incorrect (should be parameterized type of the accessor method's return type)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_REMOVE_FROM_METHOD,
				"Orphaned 'removeFrom' method for collection (no corresponding accessor method)");
		}
	}

	/**
	 * applies to properties and actions, but could be triggered against
	 * a collection (validateAddTo, validateRemoveFrom)
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkValidatePrefix() throws JavaModelException, CoreException {

		String validateMethodName = 
			MethodUtils.validateMethodNameFor(memberName);
		IMethod validateMethod = type.getMethod(validateMethodName, paramTypeSigs);

		if (foundPropertyOrCollection) {
			if (isPropertyType) {
                if (this.method.getNumberOfParameters() != 1) {
                    createProblemMarker(
                            method,
                            MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_METHOD,
                            "'validate' method for propertiy should only accept a single parameter");
                } else
				if(MethodUtils.checkExists(validateMethod) == null) {
					createProblemMarker(
						method, 
                        MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_METHOD,
						"'validate' method parameter type for property is incorrect (differs from accessor method return type)");
				}
			} else {
                // since (earlier) we look for validateAddTo & validateRemoveFrom BEFORE validate, then 
                // if we get here we have only a validate for a collection.
				createProblemMarker(
					method, 
                    MarkerConstants.ID_VALIDATE_PREFIX_INCORRECT,
					"'validate' prefix incorrect for collections - use validateAddTo or validateRemoveFrom");
			}
		} else if (foundAction) {
			if(MethodUtils.checkExists(validateMethod) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_METHOD,
					"'validate' method parameter types for action are incorrect (should be same as action method's)");
			}
		} else {
			createProblemMarker(
				method, 
                MarkerConstants.ID_ORPHANED_VALIDATE_METHOD,
				"Orphaned 'validate' method for property or action (no corresponding accessor/action method)");
		}
	}

	/**
	 * applies to collections only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkValidateAddToPrefix() throws JavaModelException, CoreException {
		
        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_ADD_TO_METHOD,
                    "'validateAddTo' method should only accept a single parameter");
            return;
        }
        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_REMOVE_FROM_METHOD,
                    "'removeFrom' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String validateAddToMethodName = 
				MethodUtils.validateAddToMethodNameFor(memberName);
            if (referencedParamTypeSigs == null) {
                // cannot check any further since collection is not generic.
                return;
            }
			IMethod validateAddToMethod = type.getMethod(validateAddToMethodName, referencedParamTypeSigs);
			if(MethodUtils.checkExists(validateAddToMethod) == null) {
				createProblemMarker(
					method,
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_ADD_TO_METHOD,
					"'validateAddTo' method parameter type for collection is incorrect (should be parameterized type of the accessor method's return type)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_VALIDATE_ADD_TO_METHOD,
				"Orphaned 'validateAddTo' method for collection (no corresponding accessor method)");
		}
	}

	/**
	 * applies to collections only
	 * @throws CoreException 
	 * @throws JavaModelException 
	 *
	 */
	private void checkValidateRemoveFromPrefix() throws JavaModelException, CoreException {

        if (this.method.getNumberOfParameters() != 1) {
            createProblemMarker(
                    method,
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_VALIDATE_REMOVE_FROM_METHOD,
                    "'validateRemoveFrom' method should only accept a single parameter");
            return;
        }
		if (foundPropertyOrCollection) {
			String validateRemoveFromMethodName = 
				MethodUtils.validateRemoveFromMethodNameFor(memberName);
            if (referencedParamTypeSigs == null) {
                // cannot check any further since collection is not generic.
                return;
            }
			IMethod validateRemoveFromMethod = type.getMethod(validateRemoveFromMethodName, referencedParamTypeSigs);
			if(MethodUtils.checkExists(validateRemoveFromMethod) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_VALIDATE_REMOVE_FROM_METHOD,
				    "'validateRemoveFrom' method parameter type for collection is incorrect (should be parameterized type of the accessor method's return type)");
			}
		} else {
			createProblemMarker(
				method, 
                MarkerConstants.ID_ORPHANED_VALIDATE_REMOVE_FROM_METHOD,
				"Orphaned 'validateRemoveFrom' method for collection (no corresponding accessor method)");
		}
	}

	/**
	 * applies to properties, collections and actions
	 * @throws CoreException 
	 */
	private void checkDisablePrefix() throws CoreException {

		if (foundPropertyOrCollection) {
			if (method.getParameterNames().length > 0) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_DISABLE_METHOD,
					"'disable' method for " + 
					propertyOrCollection(isPropertyType) + 
					" should have zero parameters.");
			}
		} else if (foundAction) {
			String disableMethodName = 
				MethodUtils.disableMethodNameFor(memberName);
			IMethod disableMethodWithArgs = type.getMethod(disableMethodName, paramTypeSigs);
            IMethod disableMethodNoArgs = type.getMethod(disableMethodName, new String[]{});
			if(MethodUtils.coalesce(disableMethodWithArgs, disableMethodNoArgs) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_DISABLE_METHOD,
					"'disable' method parameter types for action are incorrect (should be same as action method's, or have zero parameters)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_DISABLE_METHOD,
				"Orphaned 'disable' method for property, collection or action (no corresponding accessor/action method)");
		}
	}

	/**
	 * applies to properties, collections and actions
	 * @throws CoreException 
	 */
	private void checkHidePrefix() throws CoreException {
		if (foundPropertyOrCollection) {
			if (method.getParameterNames().length > 0) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_HIDE_METHOD,
					"'hide' method for " + propertyOrCollection(isPropertyType) + " should have zero parameters.");
			}
		} else if (foundAction) {
			String hideMethodName = 
				MethodUtils.hideMethodNameFor(memberName);
			IMethod hideMethodWithArgs = type.getMethod(hideMethodName, paramTypeSigs);
            IMethod hideMethodNoArgs = type.getMethod(hideMethodName, new String[]{});
			if(MethodUtils.coalesce(hideMethodWithArgs, hideMethodNoArgs) == null) {
				createProblemMarker(
					method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_HIDE_METHOD,
					"'hide' method parameter types for action are incorrect (should be same as action method's, or have zero parameters)");
			}
		} else {
			createProblemMarker(
				method,
                MarkerConstants.ID_ORPHANED_HIDE_METHOD,
				"Orphaned 'hide' method for property, collection or action (no corresponding accessor/action method)");
		}
	}

	/**
	 * applies only to properties only
	 * @throws CoreException 
	 */
	private void checkDefaultPrefix() throws CoreException {

        if (!foundPropertyOrCollection && !foundAction) {
            createProblemMarker(
                    method, 
                    MarkerConstants.ID_ORPHANED_DEFAULT_METHOD,
                    "Orphaned 'default' method (no corresponding accessor or action method)");
        }
        else if (foundPropertyOrCollection && !foundAction) {
            if (method.getParameterNames().length > 0) {
                createProblemMarker(
                    method, 
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_DEFAULT_METHOD,
                    "'default' method for property should have zero parameters.");
            }
        }
        else if (!foundPropertyOrCollection && foundAction) {
            String defaultMethodName = 
                MethodUtils.defaultMethodNameFor(memberName);
            IMethod defaultMethodWithArgs = type.getMethod(defaultMethodName, paramTypeSigs);
            IMethod defaultMethodNoArgs = type.getMethod(defaultMethodName, new String[]{});
            if(MethodUtils.coalesce(defaultMethodWithArgs, defaultMethodNoArgs) == null) {
                createProblemMarker(
                    method,
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_DEFAULT_METHOD,
                    "'default' method parameter types for action are incorrect (should be same as action method's, or have zero parameters)");
            }
        }
        else if (foundPropertyOrCollection && foundAction) {
            // shouldn't occur.
        }
    }

	/**
	 * applies to properties and actions only
	 * @throws CoreException 
	 */
	private void checkChoicesPrefix() throws CoreException {
		
        if (!foundPropertyOrCollection && !foundAction) {
            createProblemMarker(
                method, 
                MarkerConstants.ID_ORPHANED_CHOICES_METHOD,
                "Orphaned 'choices' method (no corresponding accessor or action method)");
        }
        else if (foundPropertyOrCollection && !foundAction) {
            if (method.getParameterNames().length > 0) {
                createProblemMarker(
                    method, 
                    MarkerConstants.ID_NUMBER_PARAMETERS_INCORRECT_CHOICES_METHOD,
                    "'choices' method for property should have zero parameters.");
            }
        }
        else if (!foundPropertyOrCollection && foundAction) {
            String choicesMethodName = 
                MethodUtils.choicesMethodNameFor(memberName);
            IMethod choicesMethodWithArgs = type.getMethod(choicesMethodName, paramTypeSigs);
            IMethod choicesMethodNoArgs = type.getMethod(choicesMethodName, new String[]{});
            if(MethodUtils.coalesce(choicesMethodWithArgs, choicesMethodNoArgs) == null) {
                createProblemMarker(
                    method, 
                    MarkerConstants.ID_PARAMETER_TYPE_INCORRECT_CHOICES_METHOD,
                    "'choices' method parameter types for actions are incorrect (should be same as action method's, or have zero parameters)");
            }
        }
        else if (foundPropertyOrCollection && foundAction) {
            // shouldn't occur.
        }
    }

	
	private String propertyOrCollection(boolean isPropertyType) {
		return (isPropertyType?"property":"collection");
	}

}


/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
